package com.honeypot.app.ui.dashboard.transactions.filterComponets


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.honeypot.app.ui.dashboard.transactions.data.FilterOptions
import java.io.Serializable


@Composable
fun FilterTitleItem(filter: FilterOptions<out Serializable>, onClick: () -> Unit) {
    val textColor =
        if (filter.isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
    val lineColor = if (filter.isApplied) MaterialTheme.colorScheme.primary else Color.Unspecified

    Text(
        text = filter.title,
        style = MaterialTheme.typography.bodyMedium.copy(
            color = textColor,
            fontWeight = FontWeight.Normal,
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (lineColor != Color.Unspecified) {
                    drawLine(
                        color = lineColor,
                        start = Offset(0f, size.height),
                        end = Offset(0f, 0f),
                        strokeWidth = 8.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    )
}