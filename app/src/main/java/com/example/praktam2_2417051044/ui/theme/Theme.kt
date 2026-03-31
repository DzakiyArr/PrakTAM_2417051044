package com.example.praktam2_2417051044.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val AppColorScheme = lightColorScheme(
    primary = BluePrimary,
    secondary = BlueSecondary,
    background = SoftBackground,
    surface = CardSurface,
    onPrimary = OnPrimaryText
)

@Composable
fun PrakTAM2_2417051044Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = AppColorScheme,
        typography = AppTypography,
        content = content
    )
}