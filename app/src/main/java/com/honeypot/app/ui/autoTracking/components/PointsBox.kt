package com.honeypot.app.ui.autoTracking.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.components.DrawableEndText
import com.honeypot.app.components.DrawableStartText
import com.honeypot.app.ui.theme.extendedColors


@Composable
fun PointsBox(
    text: String,
    icon: Int,
    alignment: Alignment,
    isStart: Boolean,
) {
    val modifier = Modifier
        .background(
            MaterialTheme.extendedColors.imageBg.copy(alpha = 0.5f),
            RoundedCornerShape(12.dp)
        )
        .padding(horizontal = 15.dp, vertical = 10.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(0.9f),
        contentAlignment = alignment
    ) {
        if (isStart) {
            DrawableStartText(
                text = text,
                icon = icon,
                color = MaterialTheme.colorScheme.primary,
                textSize = 12.sp,
                iconSize = 30.dp,
                iconSpace = 5.dp,
                modifier = modifier,
            )
        } else {
            DrawableEndText(
                text = text,
                icon = icon,
                color = MaterialTheme.colorScheme.primary,
                textSize = 12.sp,
                iconSize = 30.dp,
                iconSpace = 5.dp,
                modifier = modifier
            )
        }
    }
}