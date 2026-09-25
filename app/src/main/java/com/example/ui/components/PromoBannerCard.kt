package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenDark
import com.example.ui.theme.RajshahiGreenLight
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.rememberNeumorphColors

@Composable
fun PromoBannerCard(
    onPromoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val nc = rememberNeumorphColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(106.dp)
            .neumorphicRaised(
                shape = RoundedCornerShape(22.dp),
                cornerRadius = 22.dp,
                elevation = 6.dp,
                surfaceColor = RajshahiGreenDark,
                highlightColor = RajshahiGreenLight.copy(alpha = 0.5f),
                shadowColor = nc.shadow
            )
            .clickable { onPromoClick() }
            .testTag("promo_banner_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            RajshahiGreenDark,
                            Color(0xFF0F6E50),
                            RajshahiGreenPrimary
                        )
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left architectural icon & message
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .neumorphicRaised(
                                shape = CircleShape,
                                cornerRadius = 22.dp,
                                elevation = 3.dp,
                                surfaceColor = Color.White.copy(alpha = 0.15f),
                                highlightColor = Color.White.copy(alpha = 0.4f),
                                shadowColor = Color.Black.copy(alpha = 0.3f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = "Rajshahi Heritage",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "এক ক্লিকে সকল সেবা।",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "রাজশাহী এখন আরও কাছে",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Neumorphic Button
                Box(
                    modifier = Modifier
                        .neumorphicRaised(
                            shape = RoundedCornerShape(20.dp),
                            cornerRadius = 20.dp,
                            elevation = 3.dp,
                            surfaceColor = Color.White.copy(alpha = 0.2f),
                            highlightColor = Color.White.copy(alpha = 0.5f),
                            shadowColor = Color.Black.copy(alpha = 0.25f)
                        )
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("promo_action_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "স্মার্ট হতে সাথে থাকুন",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Forward",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
