package com.honeypot.app.ui.acounts.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseViewModel
import com.honeypot.app.data.model.requestModel.AccountRequestModel
import com.honeypot.app.data.model.responseModel.AccountListModel
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.data.repository.AccountRepository
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val spUtilsManager: SpUtilsManager
) : ViewModel(), BaseViewModel {

    // Get All Accounts
    private val _getAllAccounts =
        MutableStateFlow<ApiResponse<List<AccountResponseModel>>>(ApiResponse.idle())
    val getAllAccounts = _getAllAccounts.asStateFlow()


    fun loadAccounts() {
        spUtilsManager.accountData.value?.accounts?.takeIf { it.isNotEmpty() }?.let {
            _getAllAccounts.value = ApiResponse.success(it)
        } ?: run {
            getAllAccounts()
        }
    }

    fun getAllAccounts() {
        viewModelScope.launch {
            _getAllAccounts.value = ApiResponse.loading(_getAllAccounts.value.data)
            val response = handleData(_getAllAccounts.value) { accountRepository.getAllAccounts() }
            if (response is ApiResponse.Success) {
                spUtilsManager.updateAccountData(AccountListModel(response.data.orEmpty()))
                spUtilsManager.updateUser(spUtilsManager.user.value?.copy(accounts = response.data?.size))
            }
            _getAllAccounts.value = response
        }
    }



    init {
        loadAccounts()
    }


    /**
     * Create Account Api
     */
    private val _createAccount =
        MutableStateFlow<ApiResponse<AccountResponseModel>>(ApiResponse.idle())
    val createAccount = _createAccount.asStateFlow()
    fun createAccount(accountRequestModel: AccountRequestModel) {
        viewModelScope.launch {
            _createAccount.value = ApiResponse.loading()
            _createAccount.value = accountRepository.createAccount(accountRequestModel)
            delay(500)
            _createAccount.value = ApiResponse.idle()
        }
    }

    /**
     * Update Account Api
     */
    private val _updateAccount =
        MutableStateFlow<ApiResponse<AccountResponseModel>>(ApiResponse.idle())
    val updateAccount = _updateAccount.asStateFlow()
    fun updateAccount(accountId: Int, accountRequestModel: AccountRequestModel) {
        viewModelScope.launch {
            _updateAccount.value = ApiResponse.loading()
            _updateAccount.value = accountRepository.updateAccount(accountId, accountRequestModel)
            delay(500)
            _updateAccount.value = ApiResponse.idle()

        }
    }

    /**
     * Delete Account Api
     */
    private val _deleteAccount =
        MutableStateFlow<ApiResponse<AccountResponseModel>>(ApiResponse.Idle)
    val deleteAccount = _deleteAccount.asStateFlow()
    fun deleteAccount(accountId: Int) {
        viewModelScope.launch {
            _deleteAccount.value = ApiResponse.loading()
            _deleteAccount.value = accountRepository.deleteAccount(accountId)
            delay(500)
            _deleteAccount.value = ApiResponse.idle()
        }
    }


}