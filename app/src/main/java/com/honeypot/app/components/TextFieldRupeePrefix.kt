package com.honeypot.app.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
 fun TextFieldRupeePrefix() {
    Text(
        text = "₹ ", style = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.onBackground
        )
    )
}