package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenDark
import com.example.ui.theme.RajshahiGreenLight
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.neumorphicSunken
import com.example.ui.theme.rememberNeumorphColors

@Composable
fun HeaderSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onVoiceClick: () -> Unit,
    isLoggedIn: Boolean = true,
    userName: String = "",
    onAuthClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "micPulse"
    )
    val nc = rememberNeumorphColors()

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        // Emerald curved gradient background container with Neumorphic bottom bezel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            RajshahiGreenDark,
                            RajshahiGreenPrimary,
                            Color(0xFF0F7A59)
                        )
                    )
                )
        ) {
            // Landmark historic silhouette background
            Image(
                painter = painterResource(id = R.drawable.rajshahi_landmark_1790285230610),
                contentDescription = "Historic Rajshahi Landmark",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .offset(x = 50.dp, y = (-10).dp),
                alpha = 0.32f
            )

            // Dark gradient overlay for text readability
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                RajshahiGreenDark.copy(alpha = 0.95f),
                                RajshahiGreenPrimary.copy(alpha = 0.75f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Header Content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                // Top row: Logo, Brand & Actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left Brand: Logo Emblem & Text
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Neumorphic Embossed "রা" Logo Badge
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .neumorphicRaised(
                                    shape = CircleShape,
                                    cornerRadius = 25.dp,
                                    elevation = 4.dp,
                                    surfaceColor = Color.White,
                                    highlightColor = Color.White.copy(alpha = 0.8f),
                                    shadowColor = RajshahiGreenDark.copy(alpha = 0.5f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "রা",
                                color = RajshahiGreenDark,
                                fontWeight = FontWeight.Black,
                                fontSize = 24.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "স্মার্ট",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 16.sp
                            )
                            Text(
                                text = "রাজশাহী",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 26.sp
                            )
                            Text(
                                text = "সকল সেবা এক প্ল্যাটফর্মে",
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }

                    // Right Side: Auth / Login Pill & Notification Bell & Voice AI button
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Quick Auth Status / Login Button
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.22f),
                            modifier = Modifier
                                .clickable { onAuthClick() }
                                .testTag("header_auth_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isLoggedIn) Icons.Default.Person else Icons.Default.LockOpen,
                                    contentDescription = "Account",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isLoggedIn) (userName.take(4).ifBlank { "আইডি" }) else "লগইন",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // AI Voice Conversation FAB
                        Surface(
                            shape = CircleShape,
                            color = RajshahiGreenLight,
                            shadowElevation = 6.dp,
                            modifier = Modifier
                                .size(40.dp)
                                .scale(pulseScale)
                                .clickable { onVoiceClick() }
                                .testTag("voice_assistant_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = "Voice Conversation",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        // Notification Bell
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.22f),
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { onNotificationClick() }
                                .testTag("notification_button")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = Color.Red,
                                            modifier = Modifier.size(8.dp)
                                        )
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Notifications",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Smart Rajshahi District Motto Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.16f),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "স্মার্ট সেবায় সমৃদ্ধ রাজশাহী 🏛️",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Floating Neumorphic Search Bar sitting right on the curved boundary
        NeumorphicCard(
            shape = RoundedCornerShape(26.dp),
            cornerRadius = 26.dp,
            elevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .offset(y = 205.dp)
                .testTag("search_card")
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = RajshahiGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Inner Sunken Input Container
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .neumorphicSunken(
                            shape = RoundedCornerShape(16.dp),
                            surfaceColor = nc.surface,
                            highlightColor = nc.highlight,
                            shadowColor = nc.shadow
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    androidx.compose.foundation.text.BasicTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_input"),
                        decorationBox = { innerTextField ->
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "আপনি কী সেবা খুঁজছেন?",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontSize = 13.sp
                                )
                            }
                            innerTextField()
                        }
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { onSearchQueryChange("") },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Neumorphic Filter Button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .neumorphicRaised(
                            shape = CircleShape,
                            cornerRadius = 19.dp,
                            elevation = 3.dp,
                            surfaceColor = nc.surface,
                            highlightColor = nc.highlight,
                            shadowColor = nc.shadow
                        )
                        .clickable { onFilterClick() }
                        .testTag("filter_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Filter Services",
                        tint = RajshahiGreenPrimary,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

