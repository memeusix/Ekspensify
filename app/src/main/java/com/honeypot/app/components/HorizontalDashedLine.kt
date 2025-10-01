package com.honeypot.app.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun HorizontalDashedLine(
    width: Float = 2f,
    color: Color = MaterialTheme.extendedColors.primaryBorder,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    val stroke = Stroke(
        width = width,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    Canvas(modifier) {
        drawLine(
            color = color,
            start = Offset(0f, size.height / 2),
            end = Offset(size.width, size.height / 2),
            strokeWidth = stroke.width,
            pathEffect = stroke.pathEffect
        )
    }
}
