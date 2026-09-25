package com.example.ui.theme

import android.graphics.BlurMaskFilter
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Neumorphic Color Definitions ---
val NeumorphBgLight = Color(0xFFE4ECE8)
val NeumorphSurfaceLight = Color(0xFFE4ECE8)
val NeumorphHighlightLight = Color(0xFFFFFFFF)
val NeumorphShadowLight = Color(0xFFB4C2BB)
val NeumorphBorderLight = Color(0xFFD6E2DC)

val NeumorphBgDark = Color(0xFF131C18)
val NeumorphSurfaceDark = Color(0xFF17221D)
val NeumorphHighlightDark = Color(0xFF22322A)
val NeumorphShadowDark = Color(0xFF090E0C)
val NeumorphBorderDark = Color(0xFF1E2C25)

data class NeumorphColors(
    val background: Color,
    val surface: Color,
    val highlight: Color,
    val shadow: Color,
    val border: Color
)

@Composable
fun rememberNeumorphColors(): NeumorphColors {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground || 
                 MaterialTheme.colorScheme.surface == DarkSurface ||
                 MaterialTheme.colorScheme.background == NeumorphBgDark
    return if (isDark) {
        NeumorphColors(
            background = NeumorphBgDark,
            surface = NeumorphSurfaceDark,
            highlight = NeumorphHighlightDark,
            shadow = NeumorphShadowDark,
            border = NeumorphBorderDark
        )
    } else {
        NeumorphColors(
            background = NeumorphBgLight,
            surface = NeumorphSurfaceLight,
            highlight = NeumorphHighlightLight,
            shadow = NeumorphShadowLight,
            border = NeumorphBorderLight
        )
    }
}

/**
 * Neumorphic Raised modifier that gives the signature soft 3D extruded appearance.
 */
fun Modifier.neumorphicRaised(
    shape: Shape = RoundedCornerShape(20.dp),
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 5.dp,
    surfaceColor: Color,
    highlightColor: Color,
    shadowColor: Color,
    borderAlpha: Float = 0.65f
): Modifier = this
    .drawBehind {
        val cornerPx = cornerRadius.toPx()
        val elevPx = elevation.toPx()

        drawIntoCanvas { canvas ->
            // Dark Shadow (Bottom-Right)
            val paintDark = android.graphics.Paint().apply {
                color = android.graphics.Color.TRANSPARENT
                setShadowLayer(elevPx * 1.4f, elevPx * 0.7f, elevPx * 0.7f, shadowColor.toArgb())
            }
            canvas.nativeCanvas.drawRoundRect(
                0f, 0f, size.width, size.height,
                cornerPx, cornerPx,
                paintDark
            )

            // Light Highlight (Top-Left)
            val paintLight = android.graphics.Paint().apply {
                color = android.graphics.Color.TRANSPARENT
                setShadowLayer(elevPx * 1.4f, -elevPx * 0.7f, -elevPx * 0.7f, highlightColor.toArgb())
            }
            canvas.nativeCanvas.drawRoundRect(
                0f, 0f, size.width, size.height,
                cornerPx, cornerPx,
                paintLight
            )
        }
    }
    .clip(shape)
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                surfaceColor.copy(alpha = 1f),
                surfaceColor
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    )
    .border(
        width = 1.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                highlightColor.copy(alpha = borderAlpha),
                shadowColor.copy(alpha = borderAlpha * 0.5f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )

/**
 * Neumorphic Sunken modifier for recessed/inset fields (inputs, selected chips).
 */
fun Modifier.neumorphicSunken(
    shape: Shape = RoundedCornerShape(16.dp),
    surfaceColor: Color,
    highlightColor: Color,
    shadowColor: Color
): Modifier = this
    .clip(shape)
    .background(
        brush = Brush.linearGradient(
            colors = listOf(
                shadowColor.copy(alpha = 0.35f),
                surfaceColor.copy(alpha = 0.85f),
                highlightColor.copy(alpha = 0.35f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        )
    )
    .border(
        width = 1.2.dp,
        brush = Brush.linearGradient(
            colors = listOf(
                shadowColor.copy(alpha = 0.55f),
                highlightColor.copy(alpha = 0.7f)
            ),
            start = Offset(0f, 0f),
            end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
        ),
        shape = shape
    )

/**
 * Reusable Neumorphic Card container.
 */
@Composable
fun NeumorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(20.dp),
    cornerRadius: Dp = 20.dp,
    elevation: Dp = 6.dp,
    content: @Composable BoxScope.() -> Unit
) {
    val nc = rememberNeumorphColors()
    Box(
        modifier = modifier.neumorphicRaised(
            shape = shape,
            cornerRadius = cornerRadius,
            elevation = elevation,
            surfaceColor = nc.surface,
            highlightColor = nc.highlight,
            shadowColor = nc.shadow
        ),
        content = content
    )
}

/**
 * Reusable Neumorphic Interactive Button.
 */
@Composable
fun NeumorphicButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    cornerRadius: Dp = 16.dp,
    elevation: Dp = 5.dp,
    isPrimaryAccent: Boolean = false,
    content: @Composable RowScope.() -> Unit
) {
    val nc = rememberNeumorphColors()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val surfaceColor = if (isPrimaryAccent) RajshahiGreenPrimary else nc.surface
    val highlightColor = if (isPrimaryAccent) RajshahiGreenLight.copy(alpha = 0.6f) else nc.highlight
    val shadowColor = if (isPrimaryAccent) RajshahiGreenDark else nc.shadow

    val mod = if (isPressed) {
        modifier.neumorphicSunken(
            shape = shape,
            surfaceColor = surfaceColor,
            highlightColor = highlightColor,
            shadowColor = shadowColor
        )
    } else {
        modifier.neumorphicRaised(
            shape = shape,
            cornerRadius = cornerRadius,
            elevation = elevation,
            surfaceColor = surfaceColor,
            highlightColor = highlightColor,
            shadowColor = shadowColor
        )
    }

    Box(
        modifier = mod
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}

/**
 * Circular Neumorphic Icon Button.
 */
@Composable
fun NeumorphicIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color? = null,
    elevation: Dp = 4.dp
) {
    val nc = rememberNeumorphColors()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val bg = backgroundColor ?: nc.surface

    val mod = if (isPressed) {
        modifier
            .size(size)
            .neumorphicSunken(
                shape = CircleShape,
                surfaceColor = bg,
                highlightColor = nc.highlight,
                shadowColor = nc.shadow
            )
    } else {
        modifier
            .size(size)
            .neumorphicRaised(
                shape = CircleShape,
                cornerRadius = size / 2,
                elevation = elevation,
                surfaceColor = bg,
                highlightColor = nc.highlight,
                shadowColor = nc.shadow
            )
    }

    Box(
        modifier = mod.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = onClick
        ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(size * 0.52f)
        )
    }
}
