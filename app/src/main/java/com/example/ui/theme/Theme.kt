package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme =
    lightColorScheme(
        primary = GoldPrimary,
        onPrimary = Color.White,
        primaryContainer = Color(0xFFF0E4D7),
        onPrimaryContainer = Color(0xFF543722),
        secondary = GoldSecondary,
        onSecondary = Color.White,
        background = CreamBackground,
        onBackground = InkText,
        surface = PaperSurface,
        onSurface = InkText,
        surfaceVariant = Color(0xFFFDFAF6),
        onSurfaceVariant = MutedText,
        outline = BorderLine
    )

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Forced light theme per user request
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = LightColorScheme
    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
