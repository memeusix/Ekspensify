package com.honeypot.app.ui.dashboard.bottomNav.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.honeypot.app.ui.dashboard.bottomNav.BottomNavItem
import com.honeypot.app.ui.theme.Typography
import com.honeypot.app.ui.theme.extendedColors

@Composable
fun BottomNavOptions(
    item: BottomNavItem,
    isSelected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val icon = remember(isSelected) {
        if (isSelected) item.selectedIcon else item.icon
    }
    Column(
        modifier = modifier
            .padding(vertical = 14.dp)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
    ) {
        Icon(
            painterResource(icon),
            contentDescription = item.label,
            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.extendedColors.iconColor,
            modifier = Modifier
                .size(24.dp)
        )
        Text(
            item.label,
            style = Typography.bodyLarge.copy(fontSize = 10.sp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.extendedColors.iconColor
        )
    }
}