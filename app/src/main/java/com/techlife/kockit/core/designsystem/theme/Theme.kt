package com.techlife.kockit.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class KocKitExtraColors(
    val creamBackground: Color = CreamBackground,
    val pastelGreen: Color = PastelGreen,
    val primaryTeal: Color = PrimaryTeal,
    val primaryDark: Color = PrimaryDark,
    val textPrimary: Color = TextPrimary,
    val textSecondary: Color = TextSecondary,
    val cardBackground: Color = CardBackground,
    val coralAccent: Color = CoralAccent,
    val orangeAccent: Color = OrangeAccent,
    val lavenderAccent: Color = LavenderAccent,
    val borderLight: Color = BorderLight,
    val stepInactive: Color = StepInactive
)

val LocalKocKitExtraColors = staticCompositionLocalOf { KocKitExtraColors() }

private val LightColorScheme = lightColorScheme(
    primary = PrimaryTeal,
    onPrimary = White,
    primaryContainer = PrimaryTealAlt,
    secondary = LavenderAccent,
    background = CreamBackground,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    error = CoralAccent,
    onError = White
)

@Composable
fun KocKitTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalKocKitExtraColors provides KocKitExtraColors()) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = KocKitTypography,
            shapes = KocKitShapes,
            content = content
        )
    }
}

object KocKitTheme {
    val extraColors: KocKitExtraColors
        @Composable
        get() = LocalKocKitExtraColors.current
}
