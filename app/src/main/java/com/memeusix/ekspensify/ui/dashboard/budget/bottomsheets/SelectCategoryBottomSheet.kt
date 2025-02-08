package com.memeusix.ekspensify.ui.dashboard.budget.bottomsheets


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.memeusix.ekspensify.components.BottomSheetDialog
import com.memeusix.ekspensify.components.VerticalSpace
import com.memeusix.ekspensify.data.model.responseModel.CategoryResponseModel
import com.memeusix.ekspensify.ui.dashboard.budget.components.BottomSheetListItem
import com.memeusix.ekspensify.ui.dashboard.transactions.filterComponets.FilterListIcon
import com.memeusix.ekspensify.utils.BottomSheetSelectionType


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectCategoryBottomSheet(
    categoryList: List<CategoryResponseModel>,
    selectedCategoryIds: List<Int> = emptyList(),
    selectedCategoryId: Int? = null,
    selectionType: BottomSheetSelectionType,
    onDismiss: () -> Unit = {},
    onClick: (CategoryResponseModel?) -> Unit
) {
    val lazyListState = rememberLazyListState()

    BottomSheetDialog(
        title = "Select Category",
        onDismiss = onDismiss,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(5.dp),
            state = lazyListState,
        ) {
            items(
                categoryList,
                key = {
                    it.id!!
                }
            ) { item ->
                val isSelected = when (selectionType) {
                    BottomSheetSelectionType.SINGLE -> selectedCategoryId == item.id
                    BottomSheetSelectionType.MULTIPLE -> selectedCategoryIds.contains(item.id)
                }
                BottomSheetListItem(
                    title = item.name.orEmpty(),
                    isSelected = isSelected,
                    leadingContent = {
                        FilterListIcon(item.icon)
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