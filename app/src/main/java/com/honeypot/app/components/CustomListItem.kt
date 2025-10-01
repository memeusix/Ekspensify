package com.honeypot.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.ui.theme.interFontFamily

@Composable
fun CustomListItem(
    title: String,
    subtitle: String = "",
    enable: Boolean = true,
    modifier: Modifier = Modifier,
    leadingContent: @Composable () -> Unit = {},
    trailingContent: @Composable () -> Unit = {},
    desc: String = "",
    titleStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground
    ),
    shape: androidx.compose.ui.graphics.Shape? = null,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .let { if (shape != null) it.clip(shape) else it }
            .clickable(enabled = enable, onClick = onClick)
            .then(modifier),
        verticalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterVertically)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(IntrinsicSize.Min)
            ) {
            leadingContent()
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = title,
                    style = titleStyle,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                if (subtitle.isNotEmpty()) {
                    Text(
                        text = subtitle,
                        style = TextStyle(
                            fontFamily = interFontFamily,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
            trailingContent()
        }
        if (desc.isNotEmpty()) {
            Text(
                desc,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.onPrimary, RoundedCornerShape(8.dp))
                    .padding(10.dp)
            )
        }
    }

}