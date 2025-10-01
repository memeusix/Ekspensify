package com.honeypot.app.ui.dashboard.transactions.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.components.ListIcon
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.getColor

@Composable
fun SelectionOptions(
    disable: Boolean = false,
    isCategory: Boolean,
    selectedCategory: CategoryResponseModel?,
    selectedAccount: AccountResponseModel?,
    onClick: () -> Unit
) {
    val iconAndTitle = remember(selectedCategory, selectedAccount) {
        if (isCategory) {
            selectedCategory?.let { category ->
                category.icon to category.name
            }
        } else {
            selectedAccount?.let { account ->
                BankModel.getAccounts()
                    .find { it.iconSlug == account.icon }?.icon to account.name
            }
        }
    }
    val iconBg =
        if (isCategory) getColor(selectedCategory?.icFillColor).copy(0.2f) else MaterialTheme.extendedColors.imageBg

    val (icon, title) = iconAndTitle ?: (null to null)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(56.dp)
            .border(
                1.dp,
                MaterialTheme.extendedColors.primaryBorder.copy(alpha = if (disable) 0.6f else 1f),
                RoundedCornerShape(16.dp)
            )
            .alpha(if (disable) 0.6f else 1f)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val titleStyle = if (title == null) {
            MaterialTheme.typography.bodyLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        CustomListItem(
            leadingContent = {
                if (icon != null) {
                    ListIcon(icon, bgColor = iconBg)
                }
            },
            enable = !disable,
            title = title ?: if (isCategory) "Selected Category" else "Selected Account",
            titleStyle = titleStyle,
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
}