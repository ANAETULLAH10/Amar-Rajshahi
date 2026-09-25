package com.example.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.AiMessage
import com.example.data.model.EmergencyContact
import com.example.data.model.MessageSender
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.model.UserProfile
import com.example.data.remote.GeminiService
import com.example.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)
    private val repository = ServiceRepository(database.savedServiceDao())
    private val geminiService = GeminiService()

    private var tts: TextToSpeech? = null
    private val _isTtsReady = MutableStateFlow(false)

    init {
        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale.forLanguageTag("bn-BD")
                val result = tts?.setLanguage(locale)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.ENGLISH
                }
                _isTtsReady.value = true
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }

    // Navigation State
    private val _currentNavIndex = MutableStateFlow(0)
    val currentNavIndex: StateFlow<Int> = _currentNavIndex.asStateFlow()

    fun setNavIndex(index: Int) {
        _currentNavIndex.value = index
    }

    // Search & Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ServiceCategory?>(null)
    val selectedCategory: StateFlow<ServiceCategory?> = _selectedCategory.asStateFlow()

    private val _selectedUpazila = MutableStateFlow("সকল উপজেলা/থানা")
    val selectedUpazila: StateFlow<String> = _selectedUpazila.asStateFlow()

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: ServiceCategory?) {
        _selectedCategory.value = category
    }

    fun selectUpazila(upazila: String) {
        _selectedUpazila.value = upazila
    }

    // Emergency Contacts
    val emergencyContacts: List<EmergencyContact> = repository.getEmergencyContacts()

    // Upazilas list
    val upazilas: List<String> = repository.upazilas

    // Saved services from Room
    val savedServices: StateFlow<List<ServiceItem>> = repository.savedServices
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val savedCount: StateFlow<Int> = repository.savedCount
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    // User Profile & Cloud Sync
    val userProfile: StateFlow<UserProfile> = repository.userProfile
    val isCloudSynced: StateFlow<Boolean> = repository.isCloudSynced

    // Auth State & Modal
    private val _isAuthModalOpen = MutableStateFlow(false)
    val isAuthModalOpen: StateFlow<Boolean> = _isAuthModalOpen.asStateFlow()

    fun openAuthModal() {
        _isAuthModalOpen.value = true
    }

    fun closeAuthModal() {
        _isAuthModalOpen.value = false
    }

    fun login(identifier: String, pass: String): Boolean {
        if (identifier.isBlank() || pass.length < 3) return false
        val success = repository.login(identifier, pass)
        if (success) {
            _isAuthModalOpen.value = false
        }
        return success
    }

    fun signUp(name: String, phone: String, email: String, upazila: String, pass: String): Boolean {
        if (phone.isBlank() || pass.length < 3) return false
        val success = repository.signUp(name, phone, email, upazila, pass)
        if (success) {
            _isAuthModalOpen.value = false
        }
        return success
    }

    fun logout() {
        repository.logout()
    }

    fun toggleCloudSync() {
        repository.toggleCloudSync()
    }

    fun updateProfile(name: String, phone: String, upazila: String, bloodGroup: String) {
        val updated = userProfile.value.copy(
            name = name,
            phone = phone,
            upazila = upazila,
            bloodGroup = bloodGroup,
            lastSyncTime = "এখনই সিঙ্ক সম্পন্ন"
        )
        repository.updateProfile(updated)
    }

    // Filtered Services combining search, category, upazila, and saved state
    private val _allServices = MutableStateFlow(repository.getAllServices())

    val filteredServices: StateFlow<List<ServiceItem>> = combine(
        _allServices,
        _searchQuery,
        _selectedCategory,
        _selectedUpazila,
        savedServices
    ) { services, query, category, upazila, savedList ->
        val savedIds = savedList.map { it.id }.toSet()
        services.map { service ->
            service.copy(isSaved = savedIds.contains(service.id))
        }.filter { service ->
            val matchesQuery = query.isBlank() ||
                    service.titleBn.contains(query, ignoreCase = true) ||
                    service.titleEn.contains(query, ignoreCase = true) ||
                    service.subCategory.contains(query, ignoreCase = true) ||
                    service.description.contains(query, ignoreCase = true) ||
                    service.address.contains(query, ignoreCase = true)

            val matchesCategory = category == null || service.category == category

            val matchesUpazila = upazila == "সকল উপজেলা/থানা" || upazila == "সকল উপজেলা" || service.upazila == upazila

            matchesQuery && matchesCategory && matchesUpazila
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getAllServices()
    )

    // Popular Services for Home Screen
    val popularServices: StateFlow<List<ServiceItem>> = combine(_allServices, savedServices) { services, savedList ->
        val savedIds = savedList.map { it.id }.toSet()
        services.filter { it.isPopular }.map { it.copy(isSaved = savedIds.contains(it.id)) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = repository.getAllServices().filter { it.isPopular }
    )

    fun toggleBookmark(service: ServiceItem) {
        viewModelScope.launch {
            repository.toggleSaveService(service, service.isSaved)
        }
    }

    fun addNewService(
        titleBn: String,
        category: ServiceCategory,
        phone: String,
        upazila: String,
        address: String,
        description: String
    ) {
        val newId = "custom_${System.currentTimeMillis()}"
        val newService = ServiceItem(
            id = newId,
            titleBn = titleBn,
            titleEn = titleBn,
            category = category,
            subCategory = "নাগরিক প্রস্তাবিত",
            phone = phone,
            address = address,
            upazila = upazila,
            description = description,
            timing = "অনুরোধ সাপেক্ষে",
            rating = 5.0f,
            isPopular = false
        )
        repository.addCustomService(newService)
        _allServices.value = listOf(newService) + _allServices.value
    }

    // Detail Sheet / Dialog
    private val _selectedDetailService = MutableStateFlow<ServiceItem?>(null)
    val selectedDetailService: StateFlow<ServiceItem?> = _selectedDetailService.asStateFlow()

    fun showServiceDetail(service: ServiceItem) {
        _selectedDetailService.value = service
    }

    fun dismissServiceDetail() {
        _selectedDetailService.value = null
    }

    // Theme Mode
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    // AI Voice Assistant State
    private val _isVoiceAssistantOpen = MutableStateFlow(false)
    val isVoiceAssistantOpen: StateFlow<Boolean> = _isVoiceAssistantOpen.asStateFlow()

    private val _isAssistantListening = MutableStateFlow(false)
    val isAssistantListening: StateFlow<Boolean> = _isAssistantListening.asStateFlow()

    private val _isAssistantGenerating = MutableStateFlow(false)
    val isAssistantGenerating: StateFlow<Boolean> = _isAssistantGenerating.asStateFlow()

    private val _assistantMessages = MutableStateFlow<List<AiMessage>>(
        listOf(
            AiMessage(
                sender = MessageSender.AI,
                text = "আসসালামু আলাইকুম! আমি 'স্মার্ট রাজশাহী এআই ভয়েস সহকারী'। রেশম ও শিক্ষার নগরী রাজশাহীর যেকোনো জরুরি সেবা, হাসপাতালের নম্বর, ডাক্তার, ট্রেন বা বাসের সময়সূচী কিংবা ঐতিহ্যবাহী সিল্ক ও আমের তথ্য জানতে পারেন। আমি কীভাবে সাহায্য করতে পারি?"
            )
        )
    )
    val assistantMessages: StateFlow<List<AiMessage>> = _assistantMessages.asStateFlow()

    fun openVoiceAssistant() {
        _isVoiceAssistantOpen.value = true
    }

    fun closeVoiceAssistant() {
        _isVoiceAssistantOpen.value = false
        tts?.stop()
    }

    fun askVoiceAssistant(prompt: String, speakOutput: Boolean = true) {
        if (prompt.isBlank()) return
        val userMsg = AiMessage(sender = MessageSender.USER, text = prompt)
        _assistantMessages.value = _assistantMessages.value + userMsg
        _isAssistantGenerating.value = true

        viewModelScope.launch {
            val response = geminiService.askRajshahiAssistant(prompt, enableSearchGrounding = true)
            _assistantMessages.value = _assistantMessages.value + response
            _isAssistantGenerating.value = false

            if (speakOutput && _isTtsReady.value) {
                tts?.speak(response.text, TextToSpeech.QUEUE_FLUSH, null, "RajshahiAiReply")
            }
        }
    }

    fun toggleTts(message: AiMessage) {
        if (tts?.isSpeaking == true) {
            tts?.stop()
        } else {
            tts?.speak(message.text, TextToSpeech.QUEUE_FLUSH, null, "RajshahiTts")
        }
    }
}
