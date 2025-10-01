package com.honeypot.app.ui.dashboard.budget.components

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun LinearProgressBar(
    progress: Float,
    color: Color,
    modifier: Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        strokeCap = StrokeCap.Butt,
        modifier = modifier,
        gapSize = 0.dp,
        drawStopIndicator = {},
        color = color,
        trackColor = Color(0xFFE9ECED)
    )
}