package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeumorphicButton
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenDark
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.neumorphicSunken
import com.example.ui.theme.rememberNeumorphColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthDialog(
    isOpen: Boolean,
    onDismiss: () -> Unit,
    upazilas: List<String>,
    onLogin: (identifier: String, pass: String) -> Boolean,
    onSignUp: (name: String, phone: String, email: String, upazila: String, pass: String) -> Boolean
) {
    if (!isOpen) return

    var isLoginMode by remember { mutableStateOf(true) }
    var loginIdentifier by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }

    var signUpName by remember { mutableStateOf("") }
    var signUpPhone by remember { mutableStateOf("") }
    var signUpEmail by remember { mutableStateOf("") }
    var signUpUpazila by remember { mutableStateOf(upazilas.getOrElse(1) { "বোয়ালিয়া (সদর)" }) }
    var signUpPassword by remember { mutableStateOf("") }

    var showPassword by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val nc = rememberNeumorphColors()

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("auth_dialog")
    ) {
        NeumorphicCard(
            shape = RoundedCornerShape(28.dp),
            cornerRadius = 28.dp,
            elevation = 10.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = RajshahiGreenPrimary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "রা",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "স্মার্ট রাজশাহী আইডি",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "নাগরিক সেবা ও তথ্য ব্যবস্থাপনা",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("auth_dialog_close")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Neumorphic Toggle Switch (Login vs Sign Up)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .neumorphicSunken(
                            shape = RoundedCornerShape(24.dp),
                            surfaceColor = nc.surface,
                            highlightColor = nc.highlight,
                            shadowColor = nc.shadow
                        )
                        .padding(4.dp)
                ) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Login Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .then(
                                    if (isLoginMode) {
                                        Modifier.neumorphicRaised(
                                            shape = RoundedCornerShape(20.dp),
                                            cornerRadius = 20.dp,
                                            elevation = 3.dp,
                                            surfaceColor = nc.surface,
                                            highlightColor = nc.highlight,
                                            shadowColor = nc.shadow
                                        )
                                    } else Modifier
                                )
                                .clickable {
                                    isLoginMode = true
                                    errorMessage = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "লগইন",
                                fontSize = 14.sp,
                                fontWeight = if (isLoginMode) FontWeight.Bold else FontWeight.Medium,
                                color = if (isLoginMode) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Sign Up Tab
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .then(
                                    if (!isLoginMode) {
                                        Modifier.neumorphicRaised(
                                            shape = RoundedCornerShape(20.dp),
                                            cornerRadius = 20.dp,
                                            elevation = 3.dp,
                                            surfaceColor = nc.surface,
                                            highlightColor = nc.highlight,
                                            shadowColor = nc.shadow
                                        )
                                    } else Modifier
                                )
                                .clickable {
                                    isLoginMode = false
                                    errorMessage = null
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "নতুন একাউন্ট",
                                fontSize = 14.sp,
                                fontWeight = if (!isLoginMode) FontWeight.Bold else FontWeight.Medium,
                                color = if (!isLoginMode) RajshahiGreenPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Error Message if any
                errorMessage?.let { error ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .background(
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                if (isLoginMode) {
                    // --- LOGIN FORM ---
                    NeumorphicInputField(
                        value = loginIdentifier,
                        onValueChange = { loginIdentifier = it; errorMessage = null },
                        placeholder = "মোবাইল নম্বর বা ইমেইল",
                        leadingIcon = Icons.Default.Phone,
                        testTag = "login_input_identifier"
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    NeumorphicInputField(
                        value = loginPassword,
                        onValueChange = { loginPassword = it; errorMessage = null },
                        placeholder = "পাসওয়ার্ড",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        showPassword = showPassword,
                        onTogglePassword = { showPassword = !showPassword },
                        testTag = "login_input_password"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Action Button
                    NeumorphicButton(
                        onClick = {
                            if (loginIdentifier.isBlank()) {
                                errorMessage = "অনুগ্রহ করে আপনার ফোন বা ইমেইল লিখুন"
                            } else if (loginPassword.length < 3) {
                                errorMessage = "পাসওয়ার্ড কমপক্ষে ৩ অক্ষরের হতে হবে"
                            } else {
                                val ok = onLogin(loginIdentifier, loginPassword)
                                if (!ok) {
                                    errorMessage = "লগইন ব্যর্থ হয়েছে। সঠিক তথ্য দিন।"
                                }
                            }
                        },
                        isPrimaryAccent = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("auth_submit_login")
                    ) {
                        Text(
                            text = "লগইন করুন",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Demo 1-Click Login for immediate testing
                    TextButton(
                        onClick = {
                            onLogin("01712-345678", "demo123")
                        },
                        modifier = Modifier.testTag("auth_demo_login")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = null,
                            tint = RajshahiGreenPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ডেমো নাগরিক আইডিতে প্রবেশ করুন",
                            color = RajshahiGreenPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                } else {
                    // --- SIGN UP FORM ---
                    NeumorphicInputField(
                        value = signUpName,
                        onValueChange = { signUpName = it; errorMessage = null },
                        placeholder = "আপনার পূর্ণ নাম",
                        leadingIcon = Icons.Default.Person,
                        testTag = "signup_input_name"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NeumorphicInputField(
                        value = signUpPhone,
                        onValueChange = { signUpPhone = it; errorMessage = null },
                        placeholder = "মোবাইল নম্বর (যেমন 017xxxxxxxx)",
                        leadingIcon = Icons.Default.Phone,
                        keyboardType = KeyboardType.Phone,
                        testTag = "signup_input_phone"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    NeumorphicInputField(
                        value = signUpEmail,
                        onValueChange = { signUpEmail = it; errorMessage = null },
                        placeholder = "ইমেইল ঠিকানা (ঐচ্ছিক)",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        testTag = "signup_input_email"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Upazila selection display
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .neumorphicSunken(
                                shape = RoundedCornerShape(16.dp),
                                surfaceColor = nc.surface,
                                highlightColor = nc.highlight,
                                shadowColor = nc.shadow
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationCity,
                                contentDescription = null,
                                tint = RajshahiGreenPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "উপজেলা: $signUpUpazila",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    NeumorphicInputField(
                        value = signUpPassword,
                        onValueChange = { signUpPassword = it; errorMessage = null },
                        placeholder = "নতুন পাসওয়ার্ড",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        showPassword = showPassword,
                        onTogglePassword = { showPassword = !showPassword },
                        testTag = "signup_input_password"
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign Up Action Button
                    NeumorphicButton(
                        onClick = {
                            if (signUpName.isBlank()) {
                                errorMessage = "আপনার নাম লিখুন"
                            } else if (signUpPhone.length < 10) {
                                errorMessage = "সঠিক মোবাইল নম্বর প্রদান করুন"
                            } else if (signUpPassword.length < 3) {
                                errorMessage = "পাসওয়ার্ড কমপক্ষে ৩ অক্ষরের হতে হবে"
                            } else {
                                val ok = onSignUp(signUpName, signUpPhone, signUpEmail, signUpUpazila, signUpPassword)
                                if (!ok) {
                                    errorMessage = "নিবন্ধন সম্পন্ন হতে পারেনি। পুনরায় চেষ্টা করুন।"
                                }
                            }
                        },
                        isPrimaryAccent = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("auth_submit_signup")
                    ) {
                        Text(
                            text = "নিবন্ধন সম্পন্ন করুন",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NeumorphicInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    showPassword: Boolean = false,
    onTogglePassword: (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    testTag: String = ""
) {
    val nc = rememberNeumorphColors()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .neumorphicSunken(
                shape = RoundedCornerShape(16.dp),
                surfaceColor = nc.surface,
                highlightColor = nc.highlight,
                shadowColor = nc.shadow
            )
            .padding(horizontal = 14.dp, vertical = 4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = RajshahiGreenPrimary,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            androidx.compose.foundation.text.BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 12.dp)
                    .testTag(testTag),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    }
                    innerTextField()
                }
            )

            if (isPassword && onTogglePassword != null) {
                IconButton(onClick = onTogglePassword, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Toggle password",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
