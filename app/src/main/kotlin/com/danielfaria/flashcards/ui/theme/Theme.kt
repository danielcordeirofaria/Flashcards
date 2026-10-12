package com.danielfaria.flashcards.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colors for the "I knew it" / "I didn't know it" buttons and the score
val RightGreen = Color(0xFF2E7D32)
val WrongRed = Color(0xFFC62828)

private val LightColors = lightColorScheme(
    primary = Color(0xFF3F51B5),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDE1FF),
    onPrimaryContainer = Color(0xFF00105C),
    secondary = Color(0xFF00897B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB2DFDB),
    onSecondaryContainer = Color(0xFF00201C),
    tertiary = Color(0xFFEF8F00),
    background = Color(0xFFF5F6FB),
    surface = Color.White,
    surfaceVariant = Color(0xFFE6E8F2),
    onSurfaceVariant = Color(0xFF5A5D6E)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFB9C3FF),
    onPrimary = Color(0xFF08218A),
    primaryContainer = Color(0xFF283A9F),
    onPrimaryContainer = Color(0xFFDDE1FF),
    secondary = Color(0xFF80CBC4),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF00504A),
    onSecondaryContainer = Color(0xFFB2DFDB),
    tertiary = Color(0xFFFFB74D),
    background = Color(0xFF121318),
    surface = Color(0xFF1C1D24),
    surfaceVariant = Color(0xFF2C2E38),
    onSurfaceVariant = Color(0xFFC5C6D4)
)

// App theme. Uses the dark colors when the phone is in dark mode.
@Composable
fun FlashcardsTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors
    MaterialTheme(colorScheme = colors, content = content)
}
