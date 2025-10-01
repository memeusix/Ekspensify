package com.honeypot.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColors(
    val primaryBorder: Color,
    val imageBg: Color = Color.Unspecified,
    val iconColor: Color = Dark40,
    val bottomNavBg: Color = Violet40.copy(alpha = 0.1f),
    val errorSecondary: Color = Red20,
    val filterSelectionIcon :Color = Violet100
)

val LightExtendedColors = ExtendedColors(
    primaryBorder = Dark10,
    imageBg = Dark15,
    iconColor = Dark40,
    bottomNavBg = Violet40.copy(alpha = 0.1f),
    errorSecondary = Red20,
    filterSelectionIcon = Violet100
)

val DarkExtendedColors = ExtendedColors(
    primaryBorder = Dark10,
    imageBg = Dark90,
    iconColor = Light60,
    bottomNavBg = Violet120.copy(alpha = 0.2f),
    errorSecondary = Red130,
    filterSelectionIcon = VioletSecondary
)

// Default to LightColors; replaced at runtime.
val LocalExtendedColors = staticCompositionLocalOf {
    LightExtendedColors
}

val MaterialTheme.extendedColors: ExtendedColors
    @Composable
    get() = LocalExtendedColors.current
