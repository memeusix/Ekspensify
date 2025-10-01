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
import com.honeypot.app.ui.theme.Dark15
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.roundedBorder

@Composable
fun RenewalSelectionItem(
    title: String, desc: String, isSelected: Boolean = false, onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Dark15 else MaterialTheme.colorScheme.background
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else null
    val textColor = MaterialTheme.colorScheme.onBackground

    CustomListItem(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .roundedBorder(color = borderColor)
            .background(backgroundColor)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        title = title,
        subtitle = desc,
        titleStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 16.sp, fontWeight = FontWeight.Normal, color = textColor
        ),
        trailingContent = {
            Icon(
                painter = rememberAsyncImagePainter(R.drawable.ic_success),
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.extendedColors.filterSelectionIcon else MaterialTheme.extendedColors.primaryBorder,
                modifier = Modifier.size(22.dp)
            )
        },
        onClick = onClick
    )
}

