package com.honeypot.app.ui.dashboard.budget.bottomsheets


import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.honeypot.app.components.CustomDatePicker
import com.honeypot.app.data.model.requestModel.BudgetRequestModel
import com.honeypot.app.ui.dashboard.budget.viewModel.BudgetFormState
import com.honeypot.app.utils.BottomSheetSelectionType
import com.honeypot.app.utils.DatePickerType
import java.time.LocalDate

@Composable
fun HandleBottomSheets(
    createBudgetState: BudgetFormState,
    formState: BudgetRequestModel,
    datePickerType: DatePickerType,
    showCategoryBottomSheet: MutableState<Boolean>,
    showAccountBottomSheet: MutableState<Boolean>,
    showTimePeriodBottomSheet: MutableState<Boolean>,
    showDatePicker: MutableState<Boolean>,
) {

    // Category Bottom Sheet
    if (showCategoryBottomSheet.value) {
        SelectCategoryBottomSheet(categoryList = createBudgetState.categoryList,
            selectedCategoryIds = formState.categoryIds.orEmpty(),
            selectionType = BottomSheetSelectionType.MULTIPLE,
            onDismiss = { showCategoryBottomSheet.value = false },
            onClick = { category ->
                createBudgetState.updateCategoryId(category?.id)
            })
    }

    // Account Bottom Sheet
    if (showAccountBottomSheet.value) {
        SelectAccountBottomSheet(
            accountList = createBudgetState.accountList,
            selectionType = BottomSheetSelectionType.MULTIPLE,
            selectedAccountIds = formState.accountIds.orEmpty(),
            onDismiss = { showAccountBottomSheet.value = false },
            onClick = { account ->
                createBudgetState.updateAccountId(account?.id)
            })
    }
    // Time Period Bottom Sheet
    if (showTimePeriodBottomSheet.value) {
        SelectTimePeriodBottomSheet(budgetPeriodList = createBudgetState.budgetPeriodList,
            selectedTimePeriod = formState.period,
            onDismiss = { showTimePeriodBottomSheet.value = false },
            onClick = { type ->
                createBudgetState.updatePeriod(type)
                showTimePeriodBottomSheet.value = false
            })
    }

    // Date Picker
    if (showDatePicker.value) {
        CustomDatePicker(
            initialDate = when (datePickerType) {
                DatePickerType.START_DATE -> formState.startDate.let { LocalDate.parse(it) }
                    ?: LocalDate.now()

                DatePickerType.END_DATE -> formState.endDate?.let { LocalDate.parse(it) }
                    ?: formState.startDate.let { LocalDate.parse(it) }.plusDays(1) ?: LocalDate.now().plusDays(1)
            },
            minDate = if (datePickerType == DatePickerType.END_DATE) formState.startDate.let {
                LocalDate.parse(it)
            }.plusDays(1) else LocalDate.now(),
            maxDate = if (datePickerType == DatePickerType.START_DATE) {
                formState.endDate?.let { LocalDate.parse(it) } ?: LocalDate.MAX
            } else LocalDate.MAX,
            onDismiss = { showDatePicker.value = false }, onDateSelected = { date ->
                when (datePickerType) {
                    DatePickerType.START_DATE -> createBudgetState.updateStartDate(date.toString())
                    DatePickerType.END_DATE -> createBudgetState.updateEndDate(date.toString())
                }
                showDatePicker.value = false
            }   
        )
    }

}