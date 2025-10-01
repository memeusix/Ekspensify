package com.honeypot.app.ui.auth.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.honeypot.app.components.HorizontalSpace

@Composable
fun DontHaveAccountText(firstText: String, secondText: String, onclick: () -> Unit) {
    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.clickable {
        onclick()
    }) {
        Text(
            firstText,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium,
        )
        HorizontalSpace(5.dp)
        Text(
            secondText,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
