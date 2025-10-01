package com.honeypot.app.ui.acounts.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.ui.theme.interFontFamily

@Composable
fun AccountBalance(isListItem: Boolean, balance: String) {
    val fontWeight = if (isListItem) FontWeight.Medium else FontWeight.SemiBold
    val fontSize = if (isListItem) 16.sp else 20.sp
    Text(
        balance,
        fontFamily = interFontFamily,
        fontSize = fontSize,
        fontWeight = fontWeight,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(horizontal = 8.dp)
    )
}
