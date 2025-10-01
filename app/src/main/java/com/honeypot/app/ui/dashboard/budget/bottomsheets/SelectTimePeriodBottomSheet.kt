package com.honeypot.app.ui.dashboard.budget.bottomsheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.unit.dp
import com.honeypot.app.components.BottomSheetDialog
import com.honeypot.app.components.VerticalSpace
import com.honeypot.app.ui.dashboard.budget.components.BottomSheetListItem
import com.honeypot.app.utils.BudgetPeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectTimePeriodBottomSheet(
    budgetPeriodList: List<BudgetPeriod>,
    selectedTimePeriod: String? = null,
    onDismiss: () -> Unit = {},
    onClick: (String) -> Unit
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
                budgetPeriodList,
            ) { item ->
                val isSelected by rememberUpdatedState(selectedTimePeriod == item.name)
                BottomSheetListItem(
                    title = item.displayName,
                    isSelected = isSelected,
                    leadingContent = {},
                    onClick = {
                        onClick(item.name)
                    }
                )
            }
            item { VerticalSpace(5.dp) }
        }
    }
}

