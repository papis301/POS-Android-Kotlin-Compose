package com.pisco.stockmanager.desktop

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palette identique à app/.../ui/theme/Color.kt (même valeurs exactes)
val GreenPrimary = Color(0xFF1B824F)
val GreenSecondary = Color(0xFF2FA86B)
val GreenAccent = Color(0xFF81D4A5)

val SuccessGreen = Color(0xFF2E7D32)
val DangerRed = Color(0xFFD32F2F)

val BackgroundLight = Color(0xFFF5F7FA)
val SurfaceLight = Color(0xFFFFFFFF)

val TextDark = Color(0xFF1E293B)

// Couleur d'alerte "stock faible" (identique à celle codée en dur
// dans ProductPortraitScreen.kt côté Android)
val StockLowOrange = Color(0xFFFF9800)

private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GreenAccent,

    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),

    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenSecondary,
    tertiary = GreenAccent,

    background = BackgroundLight,
    surface = SurfaceLight,

    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = TextDark,
    onSurface = TextDark
)

/**
 * Équivalent Desktop de StockManagerTheme (Android).
 * Pas de dynamicColor (spécifique Android 12+, sans équivalent Desktop),
 * sinon mêmes couleurs, mêmes schémas clair/sombre.
 */
@Composable
fun YombenaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}