package com.honeypot.app.ui.dashboard.transactions.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseViewModel
import com.honeypot.app.data.model.requestModel.TransactionQueryModel
import com.honeypot.app.data.model.requestModel.TransactionRequestModel
import com.honeypot.app.data.model.responseModel.AttachmentResponseModel
import com.honeypot.app.data.model.responseModel.TransactionResponseModel
import com.honeypot.app.data.repository.TransactionRepository
import com.honeypot.app.room.repository.PendingTransactionRepo
import com.honeypot.app.ui.dashboard.transactions.data.FilterOptionProvider
import com.honeypot.app.ui.dashboard.transactions.data.SelectedFilterModel
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val pendingTransactionRepo: PendingTransactionRepo,
    val spUtilsManager: SpUtilsManager
) : ViewModel(), BaseViewModel {

    // Cached data for categories and accounts, fetched from shared preferences
    var categoryList = spUtilsManager.categoriesData.value?.categories
    var accountList = spUtilsManager.accountData.value?.accounts

    // Create Transaction
    private val _createTransaction =
        MutableStateFlow<ApiResponse<TransactionResponseModel>>(ApiResponse.idle())
    val createTransaction = _createTransaction.asStateFlow()
    fun createTransaction(transactionRequestModel: TransactionRequestModel) {
        viewModelScope.launch {
            _createTransaction.value = ApiResponse.loading()
            _createTransaction.value =
                transactionRepository.createTransaction(transactionRequestModel)
            delay(500)
            _createTransaction.value = ApiResponse.idle()
        }
    }

    // Update Transaction
    private val _updateTransaction =
        MutableStateFlow<ApiResponse<TransactionResponseModel>>(ApiResponse.idle())
    val updateTransaction = _updateTransaction.asStateFlow()
    fun updateTransaction(transactionId: Int, transactionRequestModel: TransactionRequestModel) {
        viewModelScope.launch {
            _updateTransaction.value = ApiResponse.loading()
            _updateTransaction.value =
                transactionRepository.updateTransaction(transactionId, transactionRequestModel)
            delay(500)
            _updateTransaction.value = ApiResponse.idle()
        }
    }

    // Delete Transaction
    private val _deleteTransaction =
        MutableStateFlow<ApiResponse<TransactionResponseModel>>(ApiResponse.idle())
    val deleteTransaction = _deleteTransaction.asStateFlow()
    fun deleteTransaction(transactionId: Int) {
        viewModelScope.launch {
            _deleteTransaction.value = ApiResponse.loading()
            _deleteTransaction.value = transactionRepository.deleteTransaction(transactionId)
            delay(500)
            _deleteTransaction.value = ApiResponse.idle()
        }
    }

    // Upload Attachment
    private val _uploadAttachment =
        MutableStateFlow<ApiResponse<AttachmentResponseModel>>(ApiResponse.idle())
    val uploadAttachment = _uploadAttachment.asStateFlow()
    fun uploadAttachment(imageUri: Uri) {
        viewModelScope.launch {
            _uploadAttachment.value = ApiResponse.loading()
            _uploadAttachment.value = transactionRepository.uploadAttachment(imageUri)
        }
    }


    /** ---- Filter Options and User Selections ----- **/

    // Holds the available filter options displayed to the user
    private val _availableFilterOptions = MutableStateFlow(FilterOptionProvider.getFilterOptions())
    val availableFilterOptions = _availableFilterOptions.asStateFlow()
    fun initializeFilterOptions() {
        FilterOptionProvider.initialize(spUtilsManager)
        _availableFilterOptions.value = FilterOptionProvider.getFilterOptions()
    }

    // Tracks the user's currently selected filters
    private val _userSelectedFilters = MutableStateFlow<SelectedFilterModel?>(SelectedFilterModel())
    val userSelectedFilters = _userSelectedFilters.asStateFlow()
    fun updateUserSelectedFilters(selectedFilterModel: SelectedFilterModel) {
        _userSelectedFilters.value = selectedFilterModel
    }


    /** ---- Transaction Pagination Api ---- **/

    // Holds the  query parameters used for transaction api
    private val _transactionQueryParameters = MutableStateFlow(TransactionQueryModel())
    val transactionQueryParameters = _transactionQueryParameters.asStateFlow()
    fun updateTransactionQuery(newQuery: TransactionQueryModel) {
        _transactionQueryParameters.value = newQuery
    }

    // Triggers a refresh of the transaction data
    private val _refreshTrigger = MutableStateFlow(false)
    fun refreshTransaction() {
        _refreshTrigger.value = !_refreshTrigger.value
    }

    // Fetching Transaction Using Paging 3
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _transactions: Flow<PagingData<TransactionResponseModel>> =
        combine(_transactionQueryParameters, _refreshTrigger) { filterQuery, _ ->
            filterQuery
        }.flatMapLatest { filter ->
            transactionRepository.getTransactions(filter)
        }
            .cachedIn(viewModelScope)

    fun getTransactions(): Flow<PagingData<TransactionResponseModel>> = _transactions


    /** ---- Ui States ---- **/

    // Handle Details Dialog state
    private val _transactionDetails = MutableStateFlow<TransactionResponseModel?>(null)
    val transactionDetails = _transactionDetails.asStateFlow()
    fun openDialog(details: TransactionResponseModel?) {
        _transactionDetails.value = details
    }

    fun closeDialog() {
        _transactionDetails.value = null
    }


    /** ----- Pending Transaction ---- **/
    fun deletePendingTransaction(id: Int) {
        viewModelScope.launch {
            val response = pendingTransactionRepo.deletePendingTransaction(id)
            response.fold(
                onFailure = {},
                onSuccess = {}
            )
        }
    }

}

