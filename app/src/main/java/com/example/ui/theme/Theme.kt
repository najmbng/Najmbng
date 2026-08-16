package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SleekColorScheme = lightColorScheme(
    primary = SleekPurple,
    onPrimary = Color.White,
    primaryContainer = SleekPurpleContainer,
    onPrimaryContainer = SleekPurpleOnContainer,
    secondary = CinemaGold,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),
    tertiary = ReelCrimson,
    onTertiary = Color.White,
    background = SleekBg,
    onBackground = SleekTextPrimary,
    surface = SleekSurface,
    onSurface = SleekTextPrimary,
    surfaceVariant = SleekSurfaceVariant,
    onSurfaceVariant = SleekTextSecondary,
    outline = SleekBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SleekColorScheme,
        typography = Typography,
        content = content
    )
}


