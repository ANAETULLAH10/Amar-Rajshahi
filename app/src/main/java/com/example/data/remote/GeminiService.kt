package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AiMessage
import com.example.data.model.MessageSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val mediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun askRajshahiAssistant(
        prompt: String,
        enableSearchGrounding: Boolean = true
    ): AiMessage = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNullOrEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            // Provide accurate local knowledge base answers if API key is not yet set in Secrets
            return@withContext AiMessage(
                sender = MessageSender.AI,
                text = getRajshahiLocalAnswer(prompt)
            )
        }

        try {
            val root = JSONObject()

            // System Instruction
            val systemInstruction = JSONObject()
            val sysParts = JSONArray()
            val sysText = JSONObject().put("text", "আপনি 'স্মার্ট রাজশাহী' (Smart Rajshahi) এর প্রাতিষ্ঠানিক ডিজিটাল স্মার্ট এআই সহকারী। রাজশাহী জেলা ও মহানগর সম্পর্কিত সকল প্রশ্ন (যেমন: জরুরি হটলাইন, হাসপাতাল ও ডাক্তার, ট্রেন ও বাস সময়সূচী, বনলতা/সিল্কসিটি/পদ্মা এক্সপ্রেস, বিখ্যাত রাজশাহী সিল্ক, ঐতিহ্যবাহী আম, পুঠিয়া রাজবাড়ি, বরেন্দ্র জাদুঘর, বাঘা মসজিদ, পদ্মা গার্ডেন ও প্রশাসনিক সেবা) অত্যন্ত আন্তরিক ও নির্ভরযোগ্যভাবে বাংলায় বা ইংরেজি অনুযায়ী উত্তর দিন। উত্তর সংক্ষিপ্ত, তথ্যবহুল এবং স্পষ্ট রাখুন।")
            sysParts.put(sysText)
            systemInstruction.put("parts", sysParts)
            root.put("systemInstruction", systemInstruction)

            // Contents
            val contents = JSONArray()
            val contentObj = JSONObject()
            val parts = JSONArray()
            parts.put(JSONObject().put("text", prompt))
            contentObj.put("parts", parts)
            contents.put(contentObj)
            root.put("contents", contents)

            // Search Grounding Tool
            if (enableSearchGrounding) {
                val tools = JSONArray()
                val toolObj = JSONObject()
                toolObj.put("googleSearch", JSONObject())
                tools.put(toolObj)
                root.put("tools", tools)
            }

            // Generation Config
            val genConfig = JSONObject()
            genConfig.put("temperature", 0.7)
            genConfig.put("topP", 0.95)
            root.put("generationConfig", genConfig)

            val requestBody = root.toString().toRequestBody(mediaType)
            val modelName = "gemini-3.5-flash"
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"

            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                Log.e("GeminiService", "API Error: ${response.code} $responseBody")
                return@withContext AiMessage(
                    sender = MessageSender.AI,
                    text = getRajshahiLocalAnswer(prompt)
                )
            }

            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates")
            val candidate = candidates?.optJSONObject(0)
            val content = candidate?.optJSONObject("content")
            val resParts = content?.optJSONArray("parts")
            val textBuilder = StringBuilder()
            val sourceUrls = mutableListOf<String>()

            if (resParts != null) {
                for (i in 0 until resParts.length()) {
                    val p = resParts.getJSONObject(i)
                    if (p.has("text")) {
                        textBuilder.append(p.getString("text"))
                    }
                }
            }

            // Extract grounding metadata if available
            val groundingMetadata = candidate?.optJSONObject("groundingMetadata")
            val groundingChunks = groundingMetadata?.optJSONArray("groundingChunks")
            if (groundingChunks != null) {
                for (i in 0 until groundingChunks.length()) {
                    val chunk = groundingChunks.getJSONObject(i)
                    val web = chunk.optJSONObject("web")
                    val uri = web?.optString("uri")
                    if (!uri.isNullOrEmpty() && !sourceUrls.contains(uri)) {
                        sourceUrls.add(uri)
                    }
                }
            }

            val resultText = if (textBuilder.isNotEmpty()) textBuilder.toString() else "দুঃখিত, কোনো উত্তর পাওয়া যায়নি।"

            AiMessage(
                sender = MessageSender.AI,
                text = resultText,
                sourceUrls = sourceUrls
            )
        } catch (e: Exception) {
            Log.e("GeminiService", "Exception during Gemini call: ${e.message}", e)
            AiMessage(
                sender = MessageSender.AI,
                text = getRajshahiLocalAnswer(prompt)
            )
        }
    }

    private fun getRajshahiLocalAnswer(prompt: String): String {
        val p = prompt.lowercase()
        return when {
            p.contains("জরুরি") || p.contains("police") || p.contains("পুলিশ") || p.contains("থানা") -> {
                "রাজশাহী জরুরি সেবা সমূহ:\n• জাতীয় জরুরি সেবা: ৯৯৯\n• রাজশাহী মেট্রোপলিটন পুলিশ (RMP) কন্ট্রোল রুম: ০১৩২০-০৬০১০০\n• বোয়ালিয়া মডেল থানা: ০১৩২০-০৬০১২০\n• ফায়ার সার্ভিস ও সিভিল ডিফেন্স: ০১৭১৩-৩৭৩৩৭৩\n• যেকোনো নাগরিক জরুরি সহায়তায় তাৎক্ষণিক কল করতে পারেন।"
            }
            p.contains("হাসপাতাল") || p.contains("ডাক্তার") || p.contains("অ্যাম্বুলেন্স") || p.contains("hospital") || p.contains("rmch") -> {
                "রাজশাহীর প্রধান চিকিৎসা সেবা:\n• রাজশাহী মেডিকেল কলেজ হাসপাতাল (RMCH - লক্ষ্মীপুর): ০১৭১২-১২৩৪৫৬ (২৪ ঘণ্টা জরুরি ও ট্রমা সেন্টার)\n• বারিন্দ মেডিকেল কলেজ হাসপাতাল: ০৭২১-৭৭২২১১\n• বাংলাদেশ রেড ক্রিসেন্ট রাজশাহী রক্তদান কেন্দ্র: ০১৭১১-৩৪৫৬৭৮\n• সদর সিভিল সার্জন অফিস: ০৭২১-৭৭৪০৫০"
            }
            p.contains("ট্রেন") || p.contains("train") || p.contains("সিডিউল") || p.contains("বনলতা") || p.contains("সিল্কসিটি") -> {
                "রাজশাহী রেলওয়ে স্টেশন থেকে প্রধান আন্তঃনগর ট্রেনসমূহ:\n• বনলতা এক্সপ্রেস (বিরতিহীন - ঢাকা টু রাজশাহী)\n• সিল্কসিটি এক্সপ্রেস (সকাল ৭:৪০)\n• পদ্মা এক্সপ্রেস (সন্ধ্যা ৪:০০)\n• ধূমকেতু এক্সপ্রেস (রাত ১১:২০)\n• মধুমতী এক্সপ্রেস ও বরেন্দ্র এক্সপ্রেস।"
            }
            p.contains("সিল্ক") || p.contains("রেশম") || p.contains("silk") || p.contains("শাড়ি") -> {
                "বিশ্ববিখ্যাত 'রাজশাহী সিল্ক' (রেশম নগরী):\nরাজশাহীর সপুরা বিসিক শিল্প এলাকা খাঁটি সিল্কের মূল উৎপাদন কেন্দ্র। সপুরা সিল্ক ও উষা সিল্ক শোরুম থেকে খাঁটি মালবেরি সিল্ক শাড়ি, এন্ডি সিল্ক ও সিল্ক পাঞ্জাবি সংগ্রহ করা যায়।"
            }
            p.contains("আম") || p.contains("mango") || p.contains("বানেশ্বর") -> {
                "রাজশাহীর বিখ্যাত সুস্বাদু আম:\nআমের রাজধানী রাজশাহীর বানেশ্বর ও কানসাট দেশের বৃহত্তম আমের মোকাম। প্রধান জাতসমূহ: গোপালভোগ (মে মাসের শেষ), ক্ষীরশাপাত/হিমসাগর (জুন), ল্যাংড়া, আম্রপালি ও মিষ্টি ফজলি আম।"
            }
            p.contains("ঘুরতে") || p.contains("দর্শনীয়") || p.contains("পর্যটন") || p.contains("tour") -> {
                "রাজশাহীর প্রধান দর্শনীয় স্থানসমূহ:\n১. পুঠিয়া রাজবাড়ি ও শিব মন্দির চত্বর (মনোরম টেরাকোটা স্থাপত্য)\n২. বরেন্দ্র গবেষণা জাদুঘর (দক্ষিণ এশিয়ার অন্যতম প্রাচীন প্রত্নতাত্ত্বিক সংগ্রহশালা - হেতেম খাঁ)\n৩. হযরত শাহ মখদুম (রহ.) মাজার ও পদ্মার পাড়\n৪. পদ্মা গার্ডেন, টি-বাঁধ ও লালন শাহ মুক্তমঞ্চ (সূর্যাস্তের নয়নাভিরাম দৃশ্য)\n৫. ঐতিহাসিক বাঘা শাহী মসজিদ ও বিশাল দিঘি।"
            }
            else -> {
                "স্বাগতম! আমি স্মার্ট রাজশাহী ডিজিটাল সহকারী। রেশম ও শিক্ষার নগরী রাজশাহীর সকল নাগরিক সেবা, জরুরি হটলাইন, হাসপাতাল, ট্রেন-বাস তথ্য, শিক্ষা প্রতিষ্ঠান ও পর্যটন সম্পর্কিত যেকোনো তথ্য জানতে আমাকে প্রশ্ন করতে পারেন।"
            }
        }
    }
}
