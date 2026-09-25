package com.example.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPolice
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmergencyContact
import com.example.ui.theme.EmergencyRed
import com.example.ui.theme.EmergencyRedBg
import com.example.ui.theme.EmergencyRedBorder
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.rememberNeumorphColors

@Composable
fun EmergencyServicesCard(
    emergencyContacts: List<EmergencyContact>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var selectedContact by remember { mutableStateOf<EmergencyContact?>(null) }

    val infiniteTransition = rememberInfiniteTransition(label = "sirenPulse")
    val sirenScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "siren"
    )

    NeumorphicCard(
        shape = RoundedCornerShape(22.dp),
        cornerRadius = 22.dp,
        elevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("emergency_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Title + Siren
            Row(
                modifier = Modifier.weight(1.3f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmergencyRed.copy(alpha = 0.12f),
                    modifier = Modifier
                        .size(46.dp)
                        .scale(sirenScale)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = "জরুরি সেবা",
                            tint = EmergencyRed,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Text(
                        text = "জরুরি সেবা",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmergencyRed
                    )
                    Text(
                        text = "দ্রুত সহায়তা পেতে প্রতীকে ক্লিক করুন",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF7F1D1D),
                        lineHeight = 13.sp
                    )
                }
            }

            // Vertical divider
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .width(1.dp)
                    .background(EmergencyRedBorder)
            )

            Spacer(modifier = Modifier.width(10.dp))

            // Right Quick Call Icons
            Row(
                modifier = Modifier.weight(1.7f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EmergencyIconButton(
                    title = "পুলিশ",
                    icon = Icons.Default.LocalPolice,
                    tint = Color(0xFF1565C0),
                    bgColor = Color(0xFFE3F2FD),
                    testTag = "emergency_police",
                    onClick = {
                        selectedContact = emergencyContacts.find { it.iconType == "police" }
                            ?: EmergencyContact("em_1", "পুলিশ", "Police", "রাজশাহী মেট্রোপলিটন পুলিশ ও বোয়ালিয়া থানা", "01320-060100", "police")
                    }
                )

                EmergencyIconButton(
                    title = "অ্যাম্বুলেন্স",
                    icon = Icons.Default.LocalHospital,
                    tint = Color(0xFFC62828),
                    bgColor = Color(0xFFFFEBEE),
                    testTag = "emergency_ambulance",
                    onClick = {
                        selectedContact = emergencyContacts.find { it.iconType == "ambulance" }
                            ?: EmergencyContact("em_2", "অ্যাম্বুলেন্স", "Ambulance", "রাজশাহী মেডিকেল কলেজ হাসপাতাল (RMCH)", "01712-123456", "ambulance")
                    }
                )

                EmergencyIconButton(
                    title = "ফায়ার সার্ভিস",
                    icon = Icons.Default.LocalFireDepartment,
                    tint = Color(0xFFE65100),
                    bgColor = Color(0xFFFFF3E0),
                    testTag = "emergency_fire",
                    onClick = {
                        selectedContact = emergencyContacts.find { it.iconType == "fire" }
                            ?: EmergencyContact("em_3", "ফায়ার সার্ভিস", "Fire Service", "রাজশাহী সদর ফায়ার স্টেশন", "01713-373373", "fire")
                    }
                )

                EmergencyIconButton(
                    title = "জাতীয় হেল্পলাইন",
                    icon = Icons.Default.Call,
                    tint = Color(0xFF2E7D32),
                    bgColor = Color(0xFFE8F5E9),
                    testTag = "emergency_helpline",
                    onClick = {
                        selectedContact = emergencyContacts.find { it.iconType == "helpline" }
                            ?: EmergencyContact("em_4", "জাতীয় হেল্পলাইন", "Helpline", "জরুরি সেবা ৯৯৯", "999", "helpline")
                    }
                )
            }
        }
    }

    // Call Confirmation Dialog
    selectedContact?.let { contact ->
        AlertDialog(
            onDismissRequest = { selectedContact = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = EmergencyRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = contact.nameBn,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = contact.subtitleBn,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "হটলাইন নম্বর: ${contact.number}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "আপনি কি এই নম্বরে এখনই কল করতে চান?",
                        fontSize = 13.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                            data = Uri.parse("tel:${contact.number}")
                        }
                        context.startActivity(dialIntent)
                        selectedContact = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmergencyRed),
                    modifier = Modifier.testTag("confirm_call_button")
                ) {
                    Text("কল করুন", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { selectedContact = null }) {
                    Text("বাতিল")
                }
            }
        )
    }
}

@Composable
private fun EmergencyIconButton(
    title: String,
    icon: ImageVector,
    tint: Color,
    bgColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    val nc = rememberNeumorphColors()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .neumorphicRaised(
                    shape = CircleShape,
                    cornerRadius = 23.dp,
                    elevation = 4.dp,
                    surfaceColor = bgColor,
                    highlightColor = nc.highlight.copy(alpha = 0.8f),
                    shadowColor = nc.shadow.copy(alpha = 0.7f)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1
        )
    }
}
