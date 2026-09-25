package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.ui.components.AllCategoriesRow
import com.example.ui.components.EmergencyServicesCard
import com.example.ui.components.HeaderSection
import com.example.ui.components.PopularServicesGrid
import com.example.ui.components.PromoBannerCard
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenPrimary

@Composable
fun HomeScreen(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    emergencyContacts: List<EmergencyContact>,
    popularServices: List<ServiceItem>,
    filteredServices: List<ServiceItem>,
    isCloudSynced: Boolean,
    onCategoryClick: (ServiceCategory) -> Unit,
    onViewAllServices: () -> Unit,
    onServiceClick: (ServiceItem) -> Unit,
    onToggleBookmark: (ServiceItem) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onPromoClick: () -> Unit,
    isLoggedIn: Boolean = true,
    userName: String = "",
    onAuthClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isSearching = searchQuery.isNotBlank()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // 1. Top Emerald Header & Floating Search Bar
        item {
            HeaderSection(
                searchQuery = searchQuery,
                onSearchQueryChange = onSearchQueryChange,
                onFilterClick = onFilterClick,
                onNotificationClick = onNotificationClick,
                onVoiceClick = onVoiceClick,
                isLoggedIn = isLoggedIn,
                userName = userName,
                onAuthClick = onAuthClick
            )
            // Space reserved for the floating search bar
            Spacer(modifier = Modifier.height(38.dp))
        }

        // Live Search Results View (if user is searching)
        if (isSearching) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "অনুসন্ধানের ফলাফল (${filteredServices.size})",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear search")
                    }
                }
            }

            if (filteredServices.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কোনো সেবা পাওয়া যায়নি। অন্য কিছু লিখে খুঁজুন বা এআই সহকারীকে জিজ্ঞাসা করুন।",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(filteredServices) { service ->
                    SearchResultCard(
                        service = service,
                        onClick = { onServiceClick(service) },
                        onToggleBookmark = { onToggleBookmark(service) },
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )
                }
            }
        } else {
            // Main Default Screen (Identical to Provided Screenshot)

            // Cloud Sync status bar indicator
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        imageVector = if (isCloudSynced) Icons.Default.CloudDone else Icons.Default.CloudOff,
                        contentDescription = null,
                        tint = if (isCloudSynced) TangailGreenPrimary else Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isCloudSynced) "ক্লাউড সিঙ্ক সক্রিয়" else "অফলাইন মোড",
                        fontSize = 11.sp,
                        color = if (isCloudSynced) TangailGreenPrimary else Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // 2. Emergency Services Card (জরুরি সেবা)
            item {
                EmergencyServicesCard(
                    emergencyContacts = emergencyContacts,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // 3. Popular Services Grid (জনপ্রিয় সেবা)
            item {
                PopularServicesGrid(
                    onCategoryClick = onCategoryClick,
                    onViewAllClick = onViewAllServices,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }

            // 4. All Categories Row (সকল বিভাগ)
            item {
                AllCategoriesRow(
                    onCategoryClick = onCategoryClick,
                    onViewAllClick = onViewAllServices,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }

            // 5. Promotional City Pride Banner
            item {
                PromoBannerCard(
                    onPromoClick = onPromoClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun SearchResultCard(
    service: ServiceItem,
    onClick: () -> Unit,
    onToggleBookmark: () -> Unit,
    modifier: Modifier = Modifier
) {
    NeumorphicCard(
        shape = RoundedCornerShape(18.dp),
        cornerRadius = 18.dp,
        elevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("service_item_${service.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = RajshahiGreenPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = service.category.titleBn,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = RajshahiGreenPrimary,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = service.titleBn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${service.upazila} • ${service.phone}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (service.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Bookmark",
                        tint = if (service.isSaved) EmergencyRed else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
