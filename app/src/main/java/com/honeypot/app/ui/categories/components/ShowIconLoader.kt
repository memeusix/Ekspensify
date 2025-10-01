package com.honeypot.app.ui.categories.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ShowIconLoader(modifier: Modifier = Modifier.size(32.dp)) {
    CircularProgressIndicator(
        modifier = modifier,
        strokeCap = StrokeCap.Round,
        strokeWidth = 3.dp,
        color = MaterialTheme.colorScheme.primary
    )
}
