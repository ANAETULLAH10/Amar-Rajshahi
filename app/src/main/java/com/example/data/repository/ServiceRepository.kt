package com.example.data.repository

import com.example.data.local.SavedServiceDao
import com.example.data.local.SavedServiceEntity
import com.example.data.model.EmergencyContact
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class ServiceRepository(private val dao: SavedServiceDao) {

    private val _userProfile = MutableStateFlow(UserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    private val _isCloudSynced = MutableStateFlow(true)
    val isCloudSynced: StateFlow<Boolean> = _isCloudSynced.asStateFlow()

    private val _customServices = MutableStateFlow<List<ServiceItem>>(emptyList())

    val savedServices: Flow<List<ServiceItem>> = dao.getAllSavedServices().map { entities ->
        entities.map { entity ->
            val cat = ServiceCategory.values().find { it.name == entity.categoryName } ?: ServiceCategory.LOCAL_GOVT
            ServiceItem(
                id = entity.id,
                titleBn = entity.titleBn,
                titleEn = entity.titleEn,
                category = cat,
                subCategory = "",
                phone = entity.phone,
                address = entity.address,
                upazila = entity.upazila,
                description = entity.description,
                timing = entity.timing,
                rating = entity.rating,
                isSaved = true
            )
        }
    }

    val savedCount: Flow<Int> = dao.getSavedCount()

    fun isServiceSaved(id: String): Flow<Boolean> = dao.isServiceSaved(id)

    suspend fun toggleSaveService(service: ServiceItem, isSavedNow: Boolean) {
        if (isSavedNow) {
            dao.removeSavedService(service.id)
        } else {
            dao.saveService(
                SavedServiceEntity(
                    id = service.id,
                    titleBn = service.titleBn,
                    titleEn = service.titleEn,
                    categoryName = service.category.name,
                    phone = service.phone,
                    address = service.address,
                    upazila = service.upazila,
                    description = service.description,
                    timing = service.timing,
                    rating = service.rating
                )
            )
        }
    }

    fun addCustomService(service: ServiceItem) {
        _customServices.value = listOf(service) + _customServices.value
    }

    fun updateProfile(profile: UserProfile) {
        _userProfile.value = profile
    }

    fun login(identifier: String, pass: String): Boolean {
        val current = _userProfile.value
        _userProfile.value = current.copy(
            isLoggedIn = true,
            phone = if (identifier.matches(Regex("^[0-9+ -]+$"))) identifier else current.phone,
            email = if (identifier.contains("@")) identifier else current.email
        )
        return true
    }

    fun signUp(name: String, phone: String, email: String, upazila: String, pass: String): Boolean {
        _userProfile.value = UserProfile(
            id = "user_${System.currentTimeMillis()}",
            name = name.ifBlank { "নাগরিক ব্যবহারকারী" },
            phone = phone,
            email = email.ifBlank { "citizen@rajshahi.gov.bd" },
            upazila = upazila,
            isLoggedIn = true
        )
        return true
    }

    fun logout() {
        _userProfile.value = _userProfile.value.copy(
            isLoggedIn = false
        )
    }

    fun toggleCloudSync() {
        _isCloudSynced.value = !_isCloudSynced.value
    }

    fun getEmergencyContacts(): List<EmergencyContact> = listOf(
        EmergencyContact("em_1", "পুলিশ", "Police", "রাজশাহী মেট্রোপলিটন পুলিশ (RMP) ও বোয়ালিয়া থানা", "01320-060100", "police"),
        EmergencyContact("em_2", "অ্যাম্বুলেন্স", "Ambulance", "রাজশাহী মেডিকেল কলেজ হাসপাতাল (RMCH)", "01712-123456", "ambulance"),
        EmergencyContact("em_3", "ফায়ার সার্ভিস", "Fire Service", "রাজশাহী সদর ফায়ার স্টেশন", "01713-373373", "fire"),
        EmergencyContact("em_4", "জাতীয় হেল্পলাইন", "Helpline", "জরুরি সেবা ৯৯৯ / ৩৩৩", "999", "helpline")
    )

    fun getAllServices(): List<ServiceItem> {
        val staticServices = listOf(
            // Govt
            ServiceItem(
                id = "gov_1",
                titleBn = "জেলা প্রশাসকের কার্যালয় (DC Office), রাজশাহী",
                titleEn = "DC Office Rajshahi",
                category = ServiceCategory.GOVT_ADMIN,
                subCategory = "প্রশাসনিক",
                phone = "01713-333222",
                address = "কোর্ট চত্বর, কোর্ট রোড, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "জেলা প্রশাসন, ই-সেবা কেন্দ্র, নাগরিক সনদপত্র ও ভূমি রাজস্ব সংক্রান্ত সকল সেবা।",
                timing = "সকাল ৯:০০ - বিকাল ৫:০০ (রবি-বৃহঃ)",
                rating = 4.9f,
                isPopular = true
            ),
            ServiceItem(
                id = "gov_2",
                titleBn = "রাজশাহী বিভাগীয় পাসপোর্ট ও ভিসা অফিস",
                titleEn = "Regional Passport Office Rajshahi",
                category = ServiceCategory.GOVT_ADMIN,
                subCategory = "পাসপোর্ট",
                phone = "01711-456780",
                address = "শালবাগান মোড়, রাজশাহী",
                upazila = "শাহ মখদুম",
                description = "ই-পাসপোর্ট ও এমআরপি আবেদন গ্রহণ, বায়োমেট্রিক ও ডেলিভারি সেবা।",
                timing = "সকাল ৯:০০ - বিকাল ৪:০০",
                rating = 4.6f,
                isPopular = true
            ),
            ServiceItem(
                id = "gov_3",
                titleBn = "বিআরটিএ রাজশাহী মেট্রো সার্কেল",
                titleEn = "BRTA Rajshahi Metro Circle",
                category = ServiceCategory.GOVT_ADMIN,
                subCategory = "লাইসেন্স ও মোটরযান",
                phone = "01819-876543",
                address = "নওদাপাড়া, বিমানবন্দর রোড, রাজশাহী",
                upazila = "শাহ মখদুম",
                description = "ড্রাইভিং লাইসেন্স বায়োমেট্রিক, পরীক্ষা, নবায়ন ও যানবাহনের ফিটনেস রেজিস্ট্রেশন।",
                timing = "সকাল ৯:৩০ - বিকাল ৪:৩০",
                rating = 4.5f,
                isPopular = false
            ),

            // Health
            ServiceItem(
                id = "hlth_1",
                titleBn = "রাজশাহী মেডিকেল কলেজ হাসপাতাল (RMCH)",
                titleEn = "Rajshahi Medical College Hospital",
                category = ServiceCategory.HEALTH,
                subCategory = "টারশিয়ারি হাসপাতাল ও ট্রমা সেন্টার",
                phone = "01712-123456",
                address = "লক্ষ্মীপুর মোড়, রাজশাহী",
                upazila = "রাজপাড়া",
                description = "উত্তরবঙ্গের বৃহত্তম ১২০০ শয্যা বিশিষ্ট সরকারি বিশেষায়িত হাসপাতাল ও ২৪ ঘণ্টা জরুরি সেবা।",
                timing = "২৪ ঘণ্টা খোলা",
                rating = 4.9f,
                isPopular = true,
                isEmergency = true
            ),
            ServiceItem(
                id = "hlth_2",
                titleBn = "বাংলাদেশ রেড ক্রিসেন্ট রাজশাহী রক্তদান কেন্দ্র",
                titleEn = "Red Crescent Blood Center Rajshahi",
                category = ServiceCategory.HEALTH,
                subCategory = "ব্লাড ব্যাংক ও ডোনার সমন্বয়",
                phone = "01711-345678",
                address = "লক্ষ্মীপুর ব্লাড ব্যাংক ভবন, রাজশাহী",
                upazila = "রাজপাড়া",
                description = "জরুরি রক্তের প্রয়োজনে সার্বক্ষণিক রক্ত সংগ্রহ ও নিরাপদ রক্ত সরবরাহ।",
                timing = "২৪ ঘণ্টা জরুরি সেবা",
                rating = 4.9f,
                isPopular = true,
                isEmergency = true
            ),
            ServiceItem(
                id = "hlth_3",
                titleBn = "বারিন্দ মেডিকেল কলেজ ও হাসপাতাল",
                titleEn = "Barind Medical College Hospital",
                category = ServiceCategory.HEALTH,
                subCategory = "বেসরকারি হাসপাতাল",
                phone = "0721-772211",
                address = "রাজপাড়া, রাজশাহী",
                upazila = "রাজপাড়া",
                description = "আধুনিক আইসিইউ, সিসিইউ, কিডনি ডায়ালাইসিস ও সার্বক্ষণিক ডাক্তার পরামর্শ।",
                timing = "২৪ ঘণ্টা খোলা",
                rating = 4.7f,
                isPopular = false
            ),

            // Transport
            ServiceItem(
                id = "trns_1",
                titleBn = "রাজশাহী রেলওয়ে স্টেশন (ট্রেন সিডিউল)",
                titleEn = "Rajshahi Railway Station",
                category = ServiceCategory.TRANSPORT,
                subCategory = "রেলওয়ে",
                phone = "01712-998877",
                address = "শিরোইল, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "সিল্কসিটি এক্সপ্রেস, বনলতা বিরতিহীন, পদ্মা এক্সপ্রেস, ধূমকেতু ও মধুমতী ট্রেন।",
                timing = "২৪ ঘণ্টা ট্রেন সেবা",
                rating = 4.8f,
                isPopular = true
            ),
            ServiceItem(
                id = "trns_2",
                titleBn = "শিরোইল কেন্দ্রীয় বাস টার্মিনাল (ঢাকা-রাজশাহী)",
                titleEn = "Shiroil Central Bus Terminal",
                category = ServiceCategory.TRANSPORT,
                subCategory = "বাস সার্ভিস",
                phone = "01716-112233",
                address = "শিরোইল বাস স্ট্যান্ড, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "ন্যাশনাল ট্রাভেলস, দেশ ট্রাভেলস, একতা, গ্রিনলাইন ও হানিফ এন্টারপ্রাইজ এসি/নন-এসি বাস।",
                timing = "২৪ ঘণ্টা বাস সেবা (প্রতি ১৫ মিনিট পর পর)",
                rating = 4.7f,
                isPopular = true
            ),
            ServiceItem(
                id = "trns_3",
                titleBn = "শাহ মখদুম বিমানবন্দর, রাজশাহী",
                titleEn = "Shah Makhdum Airport Rajshahi",
                category = ServiceCategory.TRANSPORT,
                subCategory = "বিমান সেবা",
                phone = "0721-761501",
                address = "নওদাপাড়া, রাজশাহী",
                upazila = "শাহ মখদুম",
                description = "বিমান বাংলাদেশ এয়ারলাইন্স, ইউএস-বাংলা ও এয়ার অ্যাস্ট্রা সরাসরি ঢাকা ফ্লাইট।",
                timing = "ফ্লাইট শিডিউল অনুযায়ী",
                rating = 4.7f,
                isPopular = true
            ),

            // Professional
            ServiceItem(
                id = "prof_1",
                titleBn = "রাজশাহী দক্ষ ইলেকট্রিশিয়ান ও সোলার টেকনিশিয়ান",
                titleEn = "Rajshahi Electrician & Solar Service",
                category = ServiceCategory.PROFESSIONAL,
                subCategory = "ইলেকট্রিক্যাল ও হোম সার্ভিস",
                phone = "01722-334455",
                address = "সাহেব বাজার, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "বাসা-বাড়ি, অফিস ওয়্যারিং, আইপিএস মেরামত ও সোলার প্যানেল ইনস্টলেশন।",
                timing = "সকাল ৮:০০ - রাত ৯:০০",
                rating = 4.8f,
                isPopular = true
            ),
            ServiceItem(
                id = "prof_2",
                titleBn = "হোম টিউটর ব্যুরো (RU ও RUET নেটওয়ার্ক)",
                titleEn = "Rajshahi RU & RUET Home Tutors",
                category = ServiceCategory.PROFESSIONAL,
                subCategory = "শিক্ষা ও গৃহশিক্ষক",
                phone = "01733-556677",
                address = "কাজলা ও তালাইমারী, রাজশাহী",
                upazila = "মতিহার",
                description = "রাজশাহী বিশ্ববিদ্যালয় ও রুয়েটের অভিজ্ঞ ও মেধাবী শিক্ষার্থীদের মাধ্যমে সকল ক্লাসের টিউটর।",
                timing = "সকাল ৯:০০ - রাত ১০:০০",
                rating = 4.9f,
                isPopular = true
            ),

            // Banking
            ServiceItem(
                id = "bnk_1",
                titleBn = "সোনালী ব্যাংক পিএলসি, কর্পোরেট শাখা",
                titleEn = "Sonali Bank PLC Corporate Branch Rajshahi",
                category = ServiceCategory.BANKING,
                subCategory = "ব্যাংকিং ও ট্রেজারি",
                phone = "0721-775123",
                address = "কোর্ট চত্বর, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "সরকারি ট্রেজারি চালান, পেনশন ভাতা, সঞ্চয়পত্র ও বৈদেশিক রেমিট্যান্স সেবা।",
                timing = "সকাল ১০:০০ - বিকাল ৪:০০ (রবি-বৃহঃ)",
                rating = 4.6f,
                isPopular = true
            ),
            ServiceItem(
                id = "bnk_2",
                titleBn = "ইসলামী ব্যাংক বাংলাদেশ পিএলসি, নিউ মার্কেট শাখা",
                titleEn = "Islami Bank New Market Branch Rajshahi",
                category = ServiceCategory.BANKING,
                subCategory = "ইসলামিক ব্যাংকিং",
                phone = "0721-774400",
                address = "নিউ মার্কেট, সাহেব বাজার, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "শরীয়াহ সম্মত ব্যাংকিং, ইনস্ট্যান্ট এটিএম ও ভিসা/মাস্টারকার্ড সুবিধা।",
                timing = "সকাল ১০:০০ - বিকাল ৪:০০",
                rating = 4.7f,
                isPopular = true
            ),

            // Courier
            ServiceItem(
                id = "cur_1",
                titleBn = "সুন্দরবন কুরিয়ার সার্ভিস, রাজশাহী প্রধান শাখা",
                titleEn = "Sundarban Courier Service Rajshahi",
                category = ServiceCategory.COURIER,
                subCategory = "পার্সেল ও ডকুমেন্ট",
                phone = "01713-098765",
                address = "আলুপট্টি মোড়, সাহেব বাজার, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "সারাদেশে পার্সেল, আম পার্সেল বুকিং ও ই-কমার্স হোম ডেলিভারি।",
                timing = "সকাল ৯:০০ - রাত ৯:০০",
                rating = 4.7f,
                isPopular = true
            ),
            ServiceItem(
                id = "cur_2",
                titleBn = "করতোয়া কুরিয়ার সার্ভিস, শিরোইল শাখা",
                titleEn = "Karatoa Courier Service Shiroil",
                category = ServiceCategory.COURIER,
                subCategory = "কুরিয়ার ও লজিস্টিকস",
                phone = "01711-223344",
                address = "রেল স্টেশন রোড, শিরোইল, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "দ্রুততম বাণিজ্যিক পার্সেল ও মালামাল পরিবহন সার্ভিস।",
                timing = "সকাল ৮:৩০ - রাত ৮:৩০",
                rating = 4.6f,
                isPopular = true
            ),

            // Car Service
            ServiceItem(
                id = "car_1",
                titleBn = "রাজশাহী হাইওয়ে ব্রেকডাউন ও মোটর ওয়ার্কশপ",
                titleEn = "Rajshahi Highway Breakdown & Garage",
                category = ServiceCategory.CAR_SERVICE,
                subCategory = "জরুরি মেকানিক ও গ্যারেজ",
                phone = "01788-776655",
                address = "নওদাপাড়া বাইপাস, রাজশাহী",
                upazila = "শাহ মখদুম",
                description = "২৪ ঘণ্টা মহাসড়কে জরুরি উদ্ধার, টায়ার ও ইঞ্জিন রিপেয়ার সার্ভিস।",
                timing = "২৪ ঘণ্টা খোলা",
                rating = 4.8f,
                isPopular = true
            ),

            // Education
            ServiceItem(
                id = "edu_1",
                titleBn = "রাজশাহী বিশ্ববিদ্যালয় (RU)",
                titleEn = "University of Rajshahi",
                category = ServiceCategory.EDUCATION,
                subCategory = "পাবলিক বিশ্ববিদ্যালয়",
                phone = "0721-750041",
                address = "মতিহার চত্বর, রাজশাহী",
                upazila = "মতিহার",
                description = "১৯৫৩ সালে প্রতিষ্ঠিত দেশের দ্বিতীয় বৃহত্তম পাবলিক বিশ্ববিদ্যালয় ও মতিহারের সবুজ ক্যাম্পাস।",
                timing = "সকাল ৯:০০ - বিকাল ৫:০০",
                rating = 4.9f,
                isPopular = true
            ),
            ServiceItem(
                id = "edu_2",
                titleBn = "রাজশাহী প্রকৌশল ও প্রযুক্তি বিশ্ববিদ্যালয় (RUET)",
                titleEn = "Rajshahi University of Engineering & Technology",
                category = ServiceCategory.EDUCATION,
                subCategory = "প্রকৌশল বিশ্ববিদ্যালয়",
                phone = "0721-750105",
                address = "তালাইমারী, রাজশাহী",
                upazila = "মতিহার",
                description = "দেশের শীর্ষস্থানীয় প্রকৌশল ও প্রযুক্তি গবেষণা বিশ্ববিদ্যালয়।",
                timing = "সকাল ৯:০০ - বিকাল ৫:০০",
                rating = 4.9f,
                isPopular = true
            ),
            ServiceItem(
                id = "edu_3",
                titleBn = "রাজশাহী কলেজ",
                titleEn = "Rajshahi College",
                category = ServiceCategory.EDUCATION,
                subCategory = "ঐতিহাসিক ডিগ্রি কলেজ",
                phone = "0721-772345",
                address = "সাহেব বাজার, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "১৮৭৩ সালে প্রতিষ্ঠিত জাতীয় বিশ্ববিদ্যালয়ের অধীনে দেশসেরা ঐতিহাসিক শ্রেষ্ঠ সরকারি কলেজ।",
                timing = "সকাল ৯:০০ - বিকাল ৪:০০",
                rating = 4.9f,
                isPopular = false
            ),

            // Trade & Commerce
            ServiceItem(
                id = "trd_1",
                titleBn = "ঐতিহ্যবাহী রাজশাহী রেশম ও সিল্ক পল্লী (সপুরা)",
                titleEn = "Famous Rajshahi Silk Showrooms (Sapura)",
                category = ServiceCategory.TRADE_COMMERCE,
                subCategory = "খাঁটি সিল্ক বস্ত্র",
                phone = "01715-667788",
                address = "সপুরা বিসিক শিল্প এলাকা, রাজশাহী",
                upazila = "শাহ মখদুম",
                description = "বিশ্ববিখ্যাত জিআই পণ্য রাজশাহী সিল্ক শাড়ি, পাঞ্জাবি ও থান কাপড়ের মূল উৎপাদন কেন্দ্র।",
                timing = "সকাল ৯:০০ - রাত ৮:০০",
                rating = 4.9f,
                isPopular = false
            ),
            ServiceItem(
                id = "trd_2",
                titleBn = "বানেশ্বর বিখ্যাত আমের আড়ত ও বাজার",
                titleEn = "Baneswar Mango Market Rajshahi",
                category = ServiceCategory.TRADE_COMMERCE,
                subCategory = "আম ও কৃষি বাজার",
                phone = "01722-990011",
                address = "বানেশ্বর বাজার, পুঠিয়া, রাজশাহী",
                upazila = "পুঠিয়া",
                description = "গোপালভোগ, ক্ষীরশাপাত (হিমসাগর), ল্যাংড়া ও ফজলি আমের দেশের দ্বিতীয় বৃহত্তম মোকাম।",
                timing = "সকাল ৬:০০ - রাত ৯:০০ (আমের মৌসুমে)",
                rating = 4.9f,
                isPopular = false
            ),

            // Tourism & Other
            ServiceItem(
                id = "tour_1",
                titleBn = "পুঠিয়া রাজবাড়ি ও প্রাচীন মন্দির কমপ্লেক্স",
                titleEn = "Puthia Rajbari & Temple Complex",
                category = ServiceCategory.TOURISM_OTHER,
                subCategory = "ঐতিহাসিক প্রত্নতত্ত্ব স্থান",
                phone = "01712-445566",
                address = "পুঠিয়া উপজেলা সদর, রাজশাহী",
                upazila = "পুঠিয়া",
                description = "পঞ্চরত্ন গোবিন্দ মন্দির, ভুবনেশ্বর শিব মন্দির ও রাজপ্রাসাদের অপরূপ টেরাকোটা নিদর্শন।",
                timing = "সকাল ৯:০০ - বিকাল ৫:০০",
                rating = 4.9f,
                isPopular = false
            ),
            ServiceItem(
                id = "tour_2",
                titleBn = "বরেন্দ্র গবেষণা জাদুঘর",
                titleEn = "Varendra Research Museum",
                category = ServiceCategory.TOURISM_OTHER,
                subCategory = "জাদুঘর ও ইতিহাস",
                phone = "0721-775345",
                address = "হেতেম খাঁ, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "১৯১০ সালে প্রতিষ্ঠিত দক্ষিণ এশিয়ার অন্যতম প্রাচীন প্রত্নতাত্ত্বিক জাদুঘর ও ভাস্কর্য সংগ্রহ।",
                timing = "সকাল ১০:০০ - বিকাল ৫:০০ (শনি-বৃহঃ)",
                rating = 4.9f,
                isPopular = false
            ),
            ServiceItem(
                id = "tour_3",
                titleBn = "ঐতিহাসিক বাঘা শাহী মসজিদ",
                titleEn = "Historic Bagha Mosque",
                category = ServiceCategory.TOURISM_OTHER,
                subCategory = "মুঘল স্থাপত্য",
                phone = "01735-889900",
                address = "বাঘা, রাজশাহী",
                upazila = "বাঘা",
                description = "১৫২৩ সালে সুলতান নসরত শাহ কর্তৃক নির্মিত পোড়ামাটির কারুকার্যময় মসজিদ ও ৫০ টাকার নোটে মুদ্রিত।",
                timing = "সকাল ৮:০০ - সন্ধ্যা ৬:০০",
                rating = 4.9f,
                isPopular = false
            ),
            ServiceItem(
                id = "tour_4",
                titleBn = "পদ্মা গার্ডেন ও টি-বাঁধ রিভার ফ্রন্ট",
                titleEn = "Padma River Garden & T-Groin",
                category = ServiceCategory.TOURISM_OTHER,
                subCategory = "প্রাকৃতিক সৌন্দর্য ও সূর্যাস্ত পয়েন্ট",
                phone = "01711-002233",
                address = "দরগাহপাড়া, বোয়ালিয়া, রাজশাহী",
                upazila = "বোয়ালিয়া (সদর)",
                description = "পদ্মা নদীর মনোরম দৃশ্য, নির্মল বাতাস, নৌকা ভ্রমণ ও উন্মুক্ত সূর্যাস্ত অবলোকন।",
                timing = "সারাদিন খোলা",
                rating = 4.8f,
                isPopular = false
            )
        )

        return _customServices.value + staticServices
    }

    val upazilas = listOf(
        "সকল উপজেলা/থানা",
        "বোয়ালিয়া (সদর)",
        "মতিহার",
        "রাজপাড়া",
        "শাহ মখদুম",
        "পবা",
        "পুঠিয়া",
        "বাঘা",
        "চারঘাট",
        "গোদাগাড়ী",
        "তানোর",
        "বাগমারা",
        "দুর্গাপুর",
        "মোহনপুর"
    )
}
