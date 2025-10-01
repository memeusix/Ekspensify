package com.honeypot.app.ui.dashboard.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun BottomSheetListItem(
    title: String,
    subtitle: String = "",
    isSelected: Boolean,
    leadingContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    CustomListItem(
        title = title,
        titleStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Normal,
        ),
        leadingContent = leadingContent,
        subtitle = subtitle,
        trailingContent = {
            if (isSelected) {
                Icon(
                    painter = rememberAsyncImagePainter(R.drawable.ic_success),
                    tint = MaterialTheme.extendedColors.filterSelectionIcon,
                    contentDescription = null,
                    modifier = Modifier
                        .size(18.dp)
                )
            }
        },
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .let { if (isSelected) it.background(MaterialTheme.extendedColors.imageBg) else it }
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick
            )
            .padding(12.dp),
        enable = false,
    )
}