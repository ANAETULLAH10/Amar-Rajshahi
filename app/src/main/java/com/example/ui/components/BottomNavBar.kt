package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.RajshahiGreenDark
import com.example.ui.theme.RajshahiGreenLight
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.neumorphicSunken
import com.example.ui.theme.rememberNeumorphColors

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    savedCount: Int = 0,
    modifier: Modifier = Modifier
) {
    val nc = rememberNeumorphColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .neumorphicRaised(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                cornerRadius = 28.dp,
                elevation = 10.dp,
                surfaceColor = nc.surface,
                highlightColor = nc.highlight,
                shadowColor = nc.shadow
            )
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 0: হোম
            NavTabItem(
                title = "হোম",
                icon = if (selectedIndex == 0) Icons.Filled.Home else Icons.Outlined.Home,
                isSelected = selectedIndex == 0,
                testTag = "nav_home",
                onClick = { onItemSelected(0) }
            )

            // 1: সেবা
            NavTabItem(
                title = "সেবা",
                icon = if (selectedIndex == 1) Icons.Filled.GridView else Icons.Outlined.GridView,
                isSelected = selectedIndex == 1,
                testTag = "nav_services",
                onClick = { onItemSelected(1) }
            )

            // 2: Center Elevated Neumorphic FAB (সেবা যোগ করুন)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .clickable { onItemSelected(2) }
                    .testTag("nav_add_service")
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .neumorphicRaised(
                            shape = CircleShape,
                            cornerRadius = 27.dp,
                            elevation = 6.dp,
                            surfaceColor = RajshahiGreenPrimary,
                            highlightColor = RajshahiGreenLight.copy(alpha = 0.6f),
                            shadowColor = RajshahiGreenDark.copy(alpha = 0.8f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Service",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "সেবা যোগ",
                    fontSize = 10.sp,
                    fontWeight = if (selectedIndex == 2) FontWeight.Bold else FontWeight.Medium,
                    color = if (selectedIndex == 2) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // 3: সংরক্ষিত (Offline Saved)
            NavTabItem(
                title = "সংরক্ষিত",
                icon = if (selectedIndex == 3) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                isSelected = selectedIndex == 3,
                badgeCount = savedCount,
                testTag = "nav_saved",
                onClick = { onItemSelected(3) }
            )

            // 4: প্রোফাইল
            NavTabItem(
                title = "প্রোফাইল",
                icon = if (selectedIndex == 4) Icons.Filled.Person else Icons.Outlined.Person,
                isSelected = selectedIndex == 4,
                testTag = "nav_profile",
                onClick = { onItemSelected(4) }
            )
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    badgeCount: Int = 0,
    testTag: String,
    onClick: () -> Unit
) {
    val nc = rememberNeumorphColors()

    val tabModifier = if (isSelected) {
        Modifier
            .neumorphicSunken(
                shape = RoundedCornerShape(16.dp),
                surfaceColor = nc.surface,
                highlightColor = nc.highlight,
                shadowColor = nc.shadow
            )
            .padding(horizontal = 10.dp, vertical = 6.dp)
    } else {
        Modifier
            .padding(horizontal = 10.dp, vertical = 6.dp)
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = tabModifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        if (badgeCount > 0) {
            BadgedBox(
                badge = {
                    Badge(
                        containerColor = RajshahiGreenPrimary
                    ) {
                        Text(text = badgeCount.toString(), fontSize = 10.sp, color = Color.White)
                    }
                }
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isSelected) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

