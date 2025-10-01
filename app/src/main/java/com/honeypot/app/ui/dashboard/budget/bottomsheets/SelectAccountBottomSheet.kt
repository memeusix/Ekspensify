package com.honeypot.app.ui.dashboard.budget.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.honeypot.app.components.BottomSheetDialog
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.ui.acounts.data.BankModel
import com.honeypot.app.ui.dashboard.budget.components.BottomSheetListItem
import com.honeypot.app.ui.dashboard.transactions.filterComponets.FilterListIcon
import com.honeypot.app.utils.BottomSheetSelectionType
import com.honeypot.app.utils.formatRupees

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectAccountBottomSheet(
    accountList: List<AccountResponseModel>,
    selectedAccountIds: List<Int> = emptyList(),
    selectedAccountId: Int? = null,
    selectionType: BottomSheetSelectionType,
    onDismiss: () -> Unit = {},
    onClick: (AccountResponseModel?) -> Unit
) {
    val lazyListState = rememberLazyListState()

    BottomSheetDialog(
        title = "Select Account",
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = lazyListState,
        ) {
            items(
                accountList,
                key = {
                    it.id!!
                }
            ) { item ->
                val isSelected = when (selectionType) {
                    BottomSheetSelectionType.SINGLE -> selectedAccountId == item.id
                    BottomSheetSelectionType.MULTIPLE -> selectedAccountIds.contains(item.id)
                }
                val icon = BankModel.getAccounts().find { it.iconSlug == item.icon }?.icon
                BottomSheetListItem(
                    title = item.name.orEmpty(),
                    isSelected = isSelected,
                    subtitle = "Balance: ${item.balance?.formatRupees()}",
                    leadingContent = {
                        FilterListIcon(icon)
                    },
                    onClick = {
                        onClick(item)
                    }
                )
            }
            item { VerticalSpace(5.dp) }
        }
    }
}

