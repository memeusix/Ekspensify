package com.honeypot.app.ui.dashboard.transactions.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.honeypot.app.R
import com.honeypot.app.components.BottomSheetDialog
import com.honeypot.app.components.CustomListItem
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.data.model.responseModel.CategoryResponseModel
import com.honeypot.app.ui.acounts.components.AccountBalance
import com.honeypot.app.ui.acounts.components.AccountIcon
import com.honeypot.app.ui.theme.extendedColors
import com.honeypot.app.utils.BottomSheetType
import com.honeypot.app.utils.formatRupees

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CategoryAndAccountBottomSheet(
    selectedCategory: MutableState<CategoryResponseModel>,
    selectedAccount: MutableState<AccountResponseModel>,
    categories: List<CategoryResponseModel>,
    accounts: List<AccountResponseModel>,
    type: BottomSheetType,
    onDismiss: () -> Unit
) {
    BottomSheetDialog(
        title = "Select ${type.getDisplayName()}",
        onDismiss = onDismiss
    ) {
        when (type) {
            BottomSheetType.CATEGORY -> {
                CategoryList(categories, selectedCategory, onDismiss)
            }

            BottomSheetType.ACCOUNT -> {
                AccountList(accounts, selectedAccount, onDismiss)
            }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun CategoryList(
    categories: List<CategoryResponseModel>,
    selectedCategory: MutableState<CategoryResponseModel>,
    onDismiss: () -> Unit
) {
    FlowRow(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        categories.forEach { category ->
            val isSelected = selectedCategory.value.id == category.id
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.extendedColors.imageBg)
                    .clickable {
                        selectedCategory.value = category
                        onDismiss()
                    }
                    .border(
                        1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                AsyncImage(
                    category.icon,
                    placeholder = painterResource(R.drawable.ic_transaction),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    category.name.orEmpty(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun AccountList(
    accounts: List<AccountResponseModel>,
    selectedAccount: MutableState<AccountResponseModel>,
    onDismiss: () -> Unit
) {
    LazyColumn() {
        items(accounts, key = { it.id!! }) { account ->
            val isSelected = selectedAccount.value.id == account.id
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        1.dp,
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 6.dp)
            ) {
                CustomListItem(
                    leadingContent = {
                        AccountIcon(account)
                    },
                    title = account.name.orEmpty(),
                    modifier = Modifier.padding(10.dp),
                    trailingContent = {
                        AccountBalance(true, account.balance?.formatRupees() ?: "0")
                    },
                    onClick = {
                        selectedAccount.value = account
                        onDismiss()
                    }
                )
            }
        }
    }
}