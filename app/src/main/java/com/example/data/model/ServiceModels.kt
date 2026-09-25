package com.example.data.model

enum class ServiceCategory(val titleBn: String, val titleEn: String) {
    GOVT_ADMIN("সরকারি ও প্রশাসনিক", "Government & Administration"),
    HEALTH("স্বাস্থ্য সেবা", "Healthcare Services"),
    TRANSPORT("পরিবহন সেবা", "Transport Services"),
    PROFESSIONAL("পেশাজীবী সেবা", "Professional Services"),
    BANKING("ব্যাংকিং সেবা", "Banking & Finance"),
    COURIER("কুরিয়ার সেবা", "Courier & Delivery"),
    CAR_SERVICE("কার সার্ভিস", "Car & Vehicle Services"),
    EDUCATION("শিক্ষা সেবা", "Educational Institutions"),
    SOCIAL_ORG("সামাজিক সংগঠন", "Social Organizations"),
    AGRICULTURE("কৃষি সেবা", "Agriculture Services"),
    TRADE_COMMERCE("ট্রেড/বাণিজ্য", "Trade & Commerce"),
    LAW_LEGAL("আইন ও আইনজীবী", "Law & Lawyers"),
    LOCAL_GOVT("স্থানীয় সেবা", "Local Municipal Services"),
    TOURISM_OTHER("অন্যান্য সেবা", "Tourism & Other Services"),
    EMERGENCY("জরুরি সেবা", "Emergency Services")
}

data class ServiceItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val category: ServiceCategory,
    val subCategory: String,
    val phone: String,
    val address: String,
    val upazila: String = "বোয়ালিয়া (সদর)",
    val description: String = "",
    val timing: String = "সকাল ৯:০০ - বিকাল ৫:০০",
    val rating: Float = 4.8f,
    val isPopular: Boolean = false,
    val isEmergency: Boolean = false,
    val website: String = "",
    val isSaved: Boolean = false
)

data class EmergencyContact(
    val id: String,
    val nameBn: String,
    val titleEn: String,
    val subtitleBn: String,
    val number: String,
    val iconType: String // "police", "ambulance", "fire", "helpline"
)

data class UserProfile(
    val id: String = "user_001",
    val name: String = "মোহাম্মদ এনায়েতুল্লাহ",
    val phone: String = "+880 1712-345678",
    val email: String = "mdanaetullah2021@gmail.com",
    val upazila: String = "বোয়ালিয়া (সদর)",
    val bloodGroup: String = "B+",
    val isVolunteerDonor: Boolean = true,
    val isCloudSynced: Boolean = true,
    val lastSyncTime: String = "আজ ১২:৪৫ PM",
    val isLoggedIn: Boolean = true
)

data class AiMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val sourceUrls: List<String> = emptyList(),
    val isAudioPlaying: Boolean = false
)

enum class MessageSender {
    USER, AI, SYSTEM
}
