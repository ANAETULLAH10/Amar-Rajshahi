package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ServiceCategory
import com.example.ui.components.BottomNavBar
import com.example.ui.components.FilterBottomSheet
import com.example.ui.components.ServiceDetailDialog
import com.example.ui.components.VoiceAssistantBottomSheet
import com.example.ui.screens.AddServiceScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.screens.ServicesScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.TangailGreenPrimary
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

            MyApplicationTheme(darkTheme = isDarkTheme) {
                SmartRajshahiApp(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SmartRajshahiApp(viewModel: MainViewModel) {
    val navIndex by viewModel.currentNavIndex.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedUpazila by viewModel.selectedUpazila.collectAsStateWithLifecycle()

    val popularServices by viewModel.popularServices.collectAsStateWithLifecycle()
    val filteredServices by viewModel.filteredServices.collectAsStateWithLifecycle()
    val savedServices by viewModel.savedServices.collectAsStateWithLifecycle()
    val savedCount by viewModel.savedCount.collectAsStateWithLifecycle()

    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val isCloudSynced by viewModel.isCloudSynced.collectAsStateWithLifecycle()
    val isDarkTheme by viewModel.isDarkTheme.collectAsStateWithLifecycle()

    val isVoiceOpen by viewModel.isVoiceAssistantOpen.collectAsStateWithLifecycle()
    val assistantMessages by viewModel.assistantMessages.collectAsStateWithLifecycle()
    val isAssistantGenerating by viewModel.isAssistantGenerating.collectAsStateWithLifecycle()
    val selectedDetailService by viewModel.selectedDetailService.collectAsStateWithLifecycle()

    var isFilterOpen by remember { mutableStateOf(false) }
    var showNotificationDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var lastBackPressTime by remember { mutableLongStateOf(0L) }
    val navigationBackStack = remember { mutableStateListOf<Int>() }

    val navigateTo: (Int) -> Unit = { targetIndex ->
        if (targetIndex != navIndex) {
            navigationBackStack.add(navIndex)
            viewModel.setNavIndex(targetIndex)
        }
    }

    val handleBack: () -> Unit = {
        when {
            isVoiceOpen -> viewModel.closeVoiceAssistant()
            isFilterOpen -> isFilterOpen = false
            showNotificationDialog -> showNotificationDialog = false
            selectedDetailService != null -> viewModel.dismissServiceDetail()
            searchQuery.isNotBlank() -> viewModel.updateSearchQuery("")
            selectedCategory != null && navIndex == 1 -> {
                viewModel.selectCategory(null)
            }
            navigationBackStack.isNotEmpty() -> {
                val previousIndex = navigationBackStack.removeAt(navigationBackStack.lastIndex)
                viewModel.setNavIndex(previousIndex)
            }
            navIndex != 0 -> {
                viewModel.selectCategory(null)
                viewModel.setNavIndex(0)
            }
            else -> {
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastBackPressTime < 2000L) {
                    (context as? ComponentActivity)?.finish()
                } else {
                    lastBackPressTime = currentTime
                    Toast.makeText(context, "অ্যাপ থেকে বের হতে আবার ব্যাক চাপুন", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    BackHandler(enabled = true) {
        handleBack()
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            bottomBar = {
                if (!isTablet) {
                    BottomNavBar(
                        selectedIndex = navIndex,
                        onItemSelected = { navigateTo(it) },
                        savedCount = savedCount
                    )
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Adaptive NavigationRail for tablets/large screens
                if (isTablet) {
                    NavigationRail(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag("tablet_nav_rail")
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "স্মার্ট রাজশাহী",
                            fontWeight = FontWeight.Bold,
                            color = TangailGreenPrimary,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        NavigationRailItem(
                            selected = navIndex == 0,
                            onClick = { navigateTo(0) },
                            icon = { Icon(Icons.Default.Home, contentDescription = "হোম") },
                            label = { Text("হোম") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TangailGreenPrimary,
                                selectedTextColor = TangailGreenPrimary
                            )
                        )

                        NavigationRailItem(
                            selected = navIndex == 1,
                            onClick = { navigateTo(1) },
                            icon = { Icon(Icons.Default.GridView, contentDescription = "সেবা") },
                            label = { Text("সেবা") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TangailGreenPrimary,
                                selectedTextColor = TangailGreenPrimary
                            )
                        )

                        NavigationRailItem(
                            selected = navIndex == 2,
                            onClick = { navigateTo(2) },
                            icon = { Icon(Icons.Default.Add, contentDescription = "যোগ") },
                            label = { Text("যোগ করুন") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TangailGreenPrimary,
                                selectedTextColor = TangailGreenPrimary
                            )
                        )

                        NavigationRailItem(
                            selected = navIndex == 3,
                            onClick = { navigateTo(3) },
                            icon = {
                                BadgedBox(badge = {
                                    if (savedCount > 0) Badge { Text("$savedCount") }
                                }) {
                                    Icon(Icons.Default.Favorite, contentDescription = "সংরক্ষিত")
                                }
                            },
                            label = { Text("সংরক্ষিত") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TangailGreenPrimary,
                                selectedTextColor = TangailGreenPrimary
                            )
                        )

                        NavigationRailItem(
                            selected = navIndex == 4,
                            onClick = { navigateTo(4) },
                            icon = { Icon(Icons.Default.Person, contentDescription = "প্রোফাইল") },
                            label = { Text("প্রোফাইল") },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = TangailGreenPrimary,
                                selectedTextColor = TangailGreenPrimary
                            )
                        )
                    }
                }

                // Main Content with smooth screen animated transitions
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .widthIn(max = 1200.dp)
                ) {
                    AnimatedContent(
                        targetState = navIndex,
                        transitionSpec = {
                            if (targetState > initialState) {
                                (slideInHorizontally { width -> width / 3 } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> -width / 3 } + fadeOut()
                                )
                            } else {
                                (slideInHorizontally { width -> -width / 3 } + fadeIn()).togetherWith(
                                    slideOutHorizontally { width -> width / 3 } + fadeOut()
                                )
                            }
                        },
                        label = "screenTransition"
                    ) { targetIndex ->
                        when (targetIndex) {
                            0 -> HomeScreen(
                                searchQuery = searchQuery,
                                onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                                emergencyContacts = viewModel.emergencyContacts,
                                popularServices = popularServices,
                                filteredServices = filteredServices,
                                isCloudSynced = isCloudSynced,
                                onCategoryClick = { category ->
                                    viewModel.selectCategory(category)
                                    navigateTo(1)
                                },
                                onViewAllServices = { navigateTo(1) },
                                onServiceClick = { viewModel.showServiceDetail(it) },
                                onToggleBookmark = { viewModel.toggleBookmark(it) },
                                onFilterClick = { isFilterOpen = true },
                                onNotificationClick = { showNotificationDialog = true },
                                onVoiceClick = { viewModel.openVoiceAssistant() },
                                onPromoClick = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("স্মার্ট রাজশাহী ডিজিটাল প্ল্যাটফর্মে স্বাগতম!")
                                    }
                                }
                            )

                            1 -> ServicesScreen(
                                services = filteredServices,
                                searchQuery = searchQuery,
                                selectedCategory = selectedCategory,
                                selectedUpazila = selectedUpazila,
                                upazilas = viewModel.upazilas,
                                onSearchChange = { viewModel.updateSearchQuery(it) },
                                onCategorySelect = { viewModel.selectCategory(it) },
                                onUpazilaSelect = { viewModel.selectUpazila(it) },
                                onServiceClick = { viewModel.showServiceDetail(it) },
                                onToggleBookmark = { viewModel.toggleBookmark(it) },
                                onBackClick = { handleBack() }
                            )

                            2 -> AddServiceScreen(
                                upazilas = viewModel.upazilas,
                                isCloudSynced = isCloudSynced,
                                onSubmitSuccess = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("নতুন সেবাটি সফলভাবে তালিকাভুক্ত হয়েছে!")
                                    }
                                },
                                onAddNewService = { title, cat, phone, upz, addr, desc ->
                                    viewModel.addNewService(title, cat, phone, upz, addr, desc)
                                },
                                onBackClick = { handleBack() }
                            )

                            3 -> SavedScreen(
                                savedServices = savedServices,
                                onServiceClick = { viewModel.showServiceDetail(it) },
                                onRemoveBookmark = { viewModel.toggleBookmark(it) },
                                onBackClick = { handleBack() }
                            )

                            4 -> ProfileScreen(
                                profile = userProfile,
                                isCloudSynced = isCloudSynced,
                                isDarkTheme = isDarkTheme,
                                onToggleCloudSync = { viewModel.toggleCloudSync() },
                                onToggleTheme = { viewModel.toggleTheme() },
                                onUpdateProfile = { name, phone, upz, bg ->
                                    viewModel.updateProfile(name, phone, upz, bg)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("প্রোফাইল তথ্য আপডেট সম্পন্ন হয়েছে!")
                                    }
                                },
                                onBackClick = { handleBack() }
                            )
                        }
                    }
                }
            }
        }
    }

    // Filter Bottom Sheet
    FilterBottomSheet(
        isOpen = isFilterOpen,
        selectedCategory = selectedCategory,
        selectedUpazila = selectedUpazila,
        upazilas = viewModel.upazilas,
        onCategorySelected = { viewModel.selectCategory(it) },
        onUpazilaSelected = { viewModel.selectUpazila(it) },
        onDismiss = { isFilterOpen = false }
    )

    // Voice Assistant Bottom Sheet
    VoiceAssistantBottomSheet(
        isOpen = isVoiceOpen,
        messages = assistantMessages,
        isGenerating = isAssistantGenerating,
        onDismiss = { viewModel.closeVoiceAssistant() },
        onSendMessage = { prompt -> viewModel.askVoiceAssistant(prompt) },
        onToggleTts = { msg -> viewModel.toggleTts(msg) }
    )

    // Service Detail Dialog
    ServiceDetailDialog(
        service = selectedDetailService,
        onDismiss = { viewModel.dismissServiceDetail() },
        onToggleBookmark = { viewModel.toggleBookmark(it) },
        onAskAi = { service ->
            viewModel.dismissServiceDetail()
            viewModel.openVoiceAssistant()
            viewModel.askVoiceAssistant("${service.titleBn} (${service.address}) সেবাটি সম্পর্কে বিস্তারিত তথ্য ও পরামর্শ দিন।")
        }
    )

    // Notification Dialog
    if (showNotificationDialog) {
        AlertDialog(
            onDismissRequest = { showNotificationDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        tint = TangailGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "স্মার্ট রাজশাহী নোটিফিকেশন",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                androidx.compose.foundation.layout.Column {
                    Card(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "📢 জরুরি রক্তদান ক্যাম্পেইন",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TangailGreenPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রাজশাহী মেডিকেল কলেজ হাসপাতালে (RMCH) বিনামূল্যে রক্তের গ্রুপ পরীক্ষা ও স্বেচ্ছা রক্তদান কর্মসূচি চলমান।",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        androidx.compose.foundation.layout.Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "🚆 ট্রেন সময়সূচী আপডেট",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = TangailGreenPrimary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রাজশাহী রেলওয়ে স্টেশনে ঢাকা অভিমুখী বনলতা এক্সপ্রেস ও সিল্কসিটি এক্সপ্রেস যথাসময়ে চলাচল করছে।",
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showNotificationDialog = false },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = TangailGreenPrimary)
                ) {
                    Text("বুঝেছি", color = Color.White)
                }
            }
        )
    }
}
