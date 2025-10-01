package com.honeypot.app.ui.dashboard.budget.viewModel

import androidx.lifecycle.ViewModel
import com.honeypot.app.data.model.requestModel.BudgetRequestModel
import com.honeypot.app.utils.BudgetPeriod
import com.honeypot.app.utils.TransactionType
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.math.BigDecimal
import javax.inject.Inject


@HiltViewModel
class BudgetFormState @Inject constructor(
    val spUtilsManager: SpUtilsManager
) : ViewModel() {

    val categoryList =
        spUtilsManager.categoriesData.value?.categories?.filter { it.type == TransactionType.DEBIT.name }
            .orEmpty()
    val accountList = spUtilsManager.accountData.value?.accounts.orEmpty()
    val budgetPeriodList = BudgetPeriod.entries

    private val _formState = MutableStateFlow(BudgetRequestModel())
    val formState = _formState.asStateFlow()

    fun updateBudgetAmount(amount: String?) {
        _formState.value = _formState.value.copy(limit = amount?.toBigDecimal() ?: BigDecimal.ZERO)
    }

    fun updateAccountId(accountId: Int?) {
        val currentAccountIds = _formState.value.accountIds.orEmpty().toMutableList()
        if (accountId != null) {
            if (currentAccountIds.contains(accountId)) {
                currentAccountIds.remove(accountId)
            } else {
                currentAccountIds.add(accountId)
            }
            _formState.value = _formState.value.copy(accountIds = currentAccountIds)
        }
    }


    fun updateCategoryId(categoryId: Int?) {
        val currentCategoryIds = _formState.value.categoryIds.orEmpty().toMutableList()
        if (categoryId != null) {
            if (currentCategoryIds.contains(categoryId)) {
                currentCategoryIds.remove(categoryId)
            } else {
                currentCategoryIds.add(categoryId)
            }
            _formState.value = _formState.value.copy(categoryIds = currentCategoryIds)
        }
    }

    fun updatePeriod(period: String) {
        _formState.value = _formState.value.copy(period = period)
    }

    fun updateType(type: String) {
        _formState.value = _formState.value.copy(type = type)
    }

    fun updateStartDate(startDate: String) {
        _formState.value = _formState.value.copy(startDate = startDate)
    }

    fun updateEndDate(endDate: String?) {
        _formState.value = _formState.value.copy(endDate = endDate)
    }
}