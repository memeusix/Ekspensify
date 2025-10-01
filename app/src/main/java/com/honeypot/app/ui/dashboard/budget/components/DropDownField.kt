package com.honeypot.app.ui.dashboard.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.utils.roundedBorder

@Composable
fun DropDownField(
    title: String,
    leading: @Composable () -> Unit = {},
    isSelected: Boolean = true,
    onClick: () -> Unit = {}
) {
    val textColor =
        if (!isSelected) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onBackground

    CustomListItem(
        leadingContent = leading,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .roundedBorder()
            .background(
                MaterialTheme.colorScheme.background,
                RoundedCornerShape(16.dp)
            )
            .padding(vertical = 12.dp, horizontal = 16.dp),
        title = title,
        titleStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = textColor
        ),
        trailingContent = {
            Icon(
                rememberAsyncImagePainter(R.drawable.ic_arrow_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(30.dp)
            )
        },
        onClick = onClick
    )
}

