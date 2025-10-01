package com.honeypot.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.honeypot.app.R


val interFontFamily = FontFamily(
    Font(R.font.inter__regular, FontWeight.Normal),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter__light, FontWeight.Light),
    Font(R.font.inter__semi_bold, FontWeight.SemiBold),
    Font(R.font.inter__medium, FontWeight.Medium),
    Font(R.font.inter_black, FontWeight.Black),
)

val fonartoFontFamily = FontFamily(
    Font(R.font.fonarto_regular, FontWeight.Normal)
)

val goodlyFontFontFamily = FontFamily(
    Font(R.font.goodly_semibold, FontWeight.SemiBold)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Black,
        fontSize = 64.sp
    ),
    titleLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp
    ),
    titleMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    bodySmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp
    ),
    labelSmall = TextStyle(
        fontFamily = interFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )

)
