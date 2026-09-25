package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bloodtype
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.NeumorphicButton
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenDark
import com.example.ui.theme.RajshahiGreenLight
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.TangailGreenDark
import com.example.ui.theme.TangailGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.rememberNeumorphColors

@Composable
fun ProfileScreen(
    profile: UserProfile,
    isCloudSynced: Boolean,
    isDarkTheme: Boolean,
    onToggleCloudSync: () -> Unit,
    onToggleTheme: () -> Unit,
    onUpdateProfile: (name: String, phone: String, upazila: String, bloodGroup: String) -> Unit,
    onOpenAuth: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var showEditDialog by remember { mutableStateOf(false) }
    val nc = rememberNeumorphColors()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen")
    ) {
        // Header
        Surface(
            color = TangailGreenPrimary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                if (onBackClick != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("profile_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "নাগরিক প্রোফাইল",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = profile.name.take(1),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TangailGreenPrimary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = profile.name,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = profile.email,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = Color(0xFF6EE7B7),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "যাচাইকৃত নাগরিক প্রোফাইল",
                                    color = Color(0xFF6EE7B7),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    IconButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier.testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Auth Status Card (Neumorphic)
            NeumorphicCard(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (profile.isLoggedIn) "লগইন অবস্থা: সক্রিয়" else "অতিথি মোড",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (profile.isLoggedIn) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (profile.isLoggedIn) "আইডি: ${profile.phone}" else "সব সুবিধা পেতে একাউন্টে লগইন বা সাইন আপ করুন",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (profile.isLoggedIn) {
                            NeumorphicButton(
                                onClick = onLogout,
                                modifier = Modifier.testTag("profile_logout_button")
                            ) {
                                Text(
                                    text = "লগআউট",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = EmergencyRed
                                )
                            }
                        } else {
                            NeumorphicButton(
                                onClick = onOpenAuth,
                                isPrimaryAccent = true,
                                modifier = Modifier.testTag("profile_login_button")
                            ) {
                                Text(
                                    text = "লগইন / সাইন আপ",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cloud Sync Status Card
            NeumorphicCard(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .neumorphicRaised(
                                        shape = CircleShape,
                                        cornerRadius = 21.dp,
                                        elevation = 3.dp,
                                        surfaceColor = RajshahiGreenPrimary.copy(alpha = 0.12f),
                                        highlightColor = nc.highlight.copy(alpha = 0.8f),
                                        shadowColor = nc.shadow.copy(alpha = 0.6f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Sync,
                                    contentDescription = null,
                                    tint = RajshahiGreenPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "রিয়েল-টাইম ক্লাউড সিঙ্ক",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = if (isCloudSynced) "সর্বশেষ সিঙ্ক: ${profile.lastSyncTime}" else "সিঙ্ক স্থগিত (অফলাইন মোড)",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isCloudSynced,
                            onCheckedChange = { onToggleCloudSync() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = RajshahiGreenPrimary
                            ),
                            modifier = Modifier.testTag("cloud_sync_switch")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dark Mode Card
            NeumorphicCard(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .neumorphicRaised(
                                    shape = CircleShape,
                                    cornerRadius = 21.dp,
                                    elevation = 3.dp,
                                    surfaceColor = MaterialTheme.colorScheme.surfaceVariant,
                                    highlightColor = nc.highlight.copy(alpha = 0.8f),
                                    shadowColor = nc.shadow.copy(alpha = 0.6f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "ডার্ক মোড (Dark Mode)",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isDarkTheme) "ডার্ক থিম সক্রিয়" else "লাইট থিম সক্রিয়",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { onToggleTheme() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = RajshahiGreenPrimary
                        ),
                        modifier = Modifier.testTag("dark_mode_switch")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Profile Info Card
            NeumorphicCard(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "নাগরিক বিবরণ",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    ProfileRowItem(
                        icon = Icons.Default.Phone,
                        label = "মোবাইল নম্বর",
                        value = profile.phone
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileRowItem(
                        icon = Icons.Default.LocationCity,
                        label = "উপজেলা / জেলা",
                        value = "${profile.upazila}, রাজশাহী"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    ProfileRowItem(
                        icon = Icons.Default.Bloodtype,
                        label = "রক্তের গ্রুপ",
                        value = "${profile.bloodGroup} (জরুরি রক্তদাতা)"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // District Heritage & Info Card
            NeumorphicCard(
                shape = RoundedCornerShape(20.dp),
                cornerRadius = 20.dp,
                elevation = 5.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RajshahiGreenPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "স্মার্ট রাজশাহী পোর্টাল সম্পর্কে",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "স্মার্ট রাজশাহী একটি সমন্বিত ডিজিটাল নাগরিক সেবা প্ল্যাটফর্ম। এতে রাজশাহী মহানগর ও জেলার সকল জরুরি নম্বর, স্বাস্থ্য, প্রশাসন, পরিবহন, পর্যটন এবং শিক্ষা প্রতিষ্ঠানের তথ্য পাওয়া যায়। জেমিনি এআই লাইভ ভয়েস এবং অফলাইন রুম ডাটাবেজ সমন্বয়ে এটি সকল পরিস্থিতিতে নিরবচ্ছিন্ন সেবা প্রদানে সক্ষম।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "সংস্করণ: ২.৪.০ (স্মার্ট বাংলাদেশ সংস্করণ)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = RajshahiGreenPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }

    // Edit Profile Dialog
    if (showEditDialog) {
        var nameInput by remember { mutableStateOf(profile.name) }
        var phoneInput by remember { mutableStateOf(profile.phone) }
        var upazilaInput by remember { mutableStateOf(profile.upazila) }
        var bloodGroupInput by remember { mutableStateOf(profile.bloodGroup) }

        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("প্রোফাইল সম্পাদনা করুন", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    OutlinedTextField(
                        value = nameInput,
                        onValueChange = { nameInput = it },
                        label = { Text("নাম") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("মোবাইল নম্বর") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = upazilaInput,
                        onValueChange = { upazilaInput = it },
                        label = { Text("উপজেলা") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = bloodGroupInput,
                        onValueChange = { bloodGroupInput = it },
                        label = { Text("রক্তের গ্রুপ") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(nameInput, phoneInput, upazilaInput, bloodGroupInput)
                        showEditDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = TangailGreenPrimary)
                ) {
                    Text("সংরক্ষণ করুন", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditDialog = false }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
private fun ProfileRowItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TangailGreenPrimary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = label,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
