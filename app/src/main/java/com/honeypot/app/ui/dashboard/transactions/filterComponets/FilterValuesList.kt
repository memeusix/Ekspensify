package com.honeypot.app.ui.dashboard.transactions.filterComponets


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.honeypot.app.R
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.dashboard.transactions.data.AmountRange
import com.honeypot.app.ui.dashboard.transactions.data.DateRange
import com.honeypot.app.ui.dashboard.transactions.data.SortBy
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.TransactionType
import java.io.Serializable

@Composable
fun FilterValuesList(item: Serializable, isSelected: Boolean, onClick: () -> Unit) {

    val itemIcon = remember(item) {
        when (item) {
            is CategoryResponseModel -> item.icon
            is AccountResponseModel -> BankModel.getAccounts()
                .find { it.iconSlug == item.icon }?.icon

            else -> null
        }
    }
    CustomListItem(
        title = when (item) {
            is CategoryResponseModel -> item.name.orEmpty()
            is AccountResponseModel -> item.name.orEmpty()
            is DateRange -> item.displayName
            is AmountRange -> item.displayName
            is SortBy -> item.displayName
            is TransactionType -> item.displayName
            else -> item.toString()
        },
        titleStyle = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Normal,
        ),
        leadingContent = {
            itemIcon?.let {
                FilterListIcon(itemIcon)
            }
        },
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
        enable = false
    )
}


@Composable
 fun FilterListIcon(item: Any?) {
    AsyncImage(
        model = item,
        contentDescription = null,
        modifier = Modifier
            .size(18.dp)
    )
}

