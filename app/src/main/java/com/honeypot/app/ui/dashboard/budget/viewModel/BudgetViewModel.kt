package com.honeypot.app.ui.dashboard.budget.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseViewModel
import com.honeypot.app.data.model.BudgetMeta
import com.honeypot.app.data.model.requestModel.BudgetQueryModel
import com.honeypot.app.data.model.requestModel.BudgetRequestModel
import com.honeypot.app.data.model.responseModel.BudgetReportResponseModel
import com.honeypot.app.data.model.responseModel.BudgetResponseModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.data.repository.BudgetRepository
import com.honeypot.app.utils.BudgetStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel(), BaseViewModel {

    // Create Budget
    private val _createBudget =
        MutableStateFlow<ApiResponse<BudgetResponseModel>>(ApiResponse.idle())
    val createBudget = _createBudget.asStateFlow()
    fun createBudget(budgetRequestModel: BudgetRequestModel) {
        viewModelScope.launch {
            _createBudget.value = ApiResponse.loading()
            _createBudget.value = budgetRepository.createBudget(budgetRequestModel)
            delay(500)
            _createBudget.value = ApiResponse.idle()
        }
    }

    // Delete Budget
    private val _deleteBudget =
        MutableStateFlow<ApiResponse<BudgetResponseModel>>(ApiResponse.idle())
    val deleteBudget = _deleteBudget.asStateFlow()
    fun deleteBudget(budgetId: Int) {
        viewModelScope.launch {
            _deleteBudget.value = ApiResponse.loading()
            _deleteBudget.value = budgetRepository.deleteBudget(budgetId)
            delay(500)
            _deleteBudget.value = ApiResponse.idle()
        }
    }

    // Get Budget by Id
    private val _budgetDetails =
        MutableStateFlow<ApiResponse<BudgetResponseModel>>(ApiResponse.idle())
    val budgetDetails = _budgetDetails.asStateFlow()
    fun getBudgetDetails(budgetId: Int) {
        viewModelScope.launch {
            _budgetDetails.value = ApiResponse.loading(_budgetDetails.value.data)
            _budgetDetails.value =
                handleData(_budgetDetails.value) { budgetRepository.getBudgetById(budgetId) }
        }
    }

    // Close Budget
    private val _updateBudget =
        MutableStateFlow<ApiResponse<BudgetResponseModel>>(ApiResponse.idle())
    val updateBudget = _updateBudget.asStateFlow()
    fun closeBudget(budgetId: Int) {
        viewModelScope.launch {
            _updateBudget.value = ApiResponse.loading()
            val response = budgetRepository.updateBudget(budgetId, BudgetStatus.CLOSED.name)
            _updateBudget.value = response
            _budgetDetails.value = response
            delay(500)
            _deleteBudget.value = ApiResponse.idle()
        }
    }

    /**
     * storing budget list metaData gor for filters
     */
    private val _budgetMeta = MutableStateFlow<BudgetMeta?>(BudgetMeta())
    val budgetMeta = _budgetMeta.asStateFlow()

    /**
     * GET ALL BUDGETS
     */
    private val _budgetQuery = MutableStateFlow(BudgetQueryModel())
    val budgetQuery = _budgetQuery.asStateFlow()
    fun updateBudgetQuery(newQuery: BudgetQueryModel) {
        _budgetQuery.value = newQuery
    }

    private val _refreshTrigger = MutableStateFlow(false)
    fun refreshBudgets() {
        _refreshTrigger.value = !_refreshTrigger.value
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _budgets: Flow<PagingData<BudgetResponseModel>> =
        combine(_budgetQuery, _refreshTrigger) { budgetQueryModel, _ ->
            budgetQueryModel
        }.flatMapLatest { query ->
            budgetRepository.getBudgets(query) { metaData ->
                _budgetMeta.value = metaData
            }
        }.cachedIn(viewModelScope)

    fun getBudgets(): Flow<PagingData<BudgetResponseModel>> = _budgets


    /**
     * GET BUDGET REPORT
     */

    private val budgetId = MutableStateFlow<Int?>(null)
    fun setBudgetId(budgetId: Int?) {
        this.budgetId.value = budgetId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _budgetReport: Flow<PagingData<BudgetReportResponseModel>> =
        combine(_budgetQuery, budgetId) { budgetQueryModel, budgetId ->
            budgetQueryModel to budgetId
        }.flatMapLatest { (budgetQuery, budgetId) ->
            if (budgetId != null) {
                budgetRepository.getBudgetReport(budgetId, budgetQuery)
            } else {
                flowOf(PagingData.empty())
            }
        }.cachedIn(viewModelScope)

    fun getBudgetReport(): Flow<PagingData<BudgetReportResponseModel>> = _budgetReport

    /**
     * GET BUDGET TRANSACTIONS
     */

    private val reportId = MutableStateFlow<Int?>(null)
    fun setReportId(reportId: Int?) {
        this.reportId.value = reportId
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private var _budgetTransaction: Flow<PagingData<TransactionResponseModel>> =
        combine(
            _budgetQuery,
            budgetId,
            reportId
        ) { budgetQueryModel, budgetId, reportId ->
            Triple(budgetQueryModel, budgetId, reportId)
        }.flatMapLatest { (budgetQuery, budgetId, reportId) ->
            if (budgetId != null && reportId != null) {
                budgetRepository.getBudgetTransaction(budgetId, reportId, budgetQuery)
            } else {
                flowOf(PagingData.empty())
            }
        }.cachedIn(viewModelScope)

    fun getBudgetTransaction(): Flow<PagingData<TransactionResponseModel>> = _budgetTransaction
}