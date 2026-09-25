package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = DarkGreenPrimary,
    onPrimary = Color.Black,
    primaryContainer = TangailGreenDark,
    onPrimaryContainer = Color.White,
    secondary = TangailGreenLight,
    onSecondary = Color.Black,
    background = NeumorphBgDark,
    onBackground = DarkTextPrimary,
    surface = NeumorphSurfaceDark,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    error = EmergencyRed,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = TangailGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = TangailGreenPill,
    onPrimaryContainer = TangailGreenDark,
    secondary = TangailGreenLight,
    onSecondary = Color.White,
    background = NeumorphBgLight,
    onBackground = TextPrimaryLight,
    surface = NeumorphSurfaceLight,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFDCE6E1),
    onSurfaceVariant = TextSecondaryLight,
    error = EmergencyRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent district emerald green branding
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
