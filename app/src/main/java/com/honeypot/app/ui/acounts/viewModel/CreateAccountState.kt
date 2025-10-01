package com.honeypot.app.ui.acounts.viewModel

import androidx.lifecycle.ViewModel
import com.honeypot.app.data.model.responseModel.AccountResponseModel
import com.honeypot.app.ui.acounts.data.BankModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateAccountState @Inject constructor() : ViewModel() {

    private val _walletList = MutableStateFlow(BankModel.getWallet())
    val walletList = _walletList.asStateFlow()

    private val _bankList = MutableStateFlow(BankModel.getBanks())
    val bankList = _bankList.asStateFlow()

    private val _selectedBank = MutableStateFlow<BankModel?>(null)
    val selectedBank = _selectedBank.asStateFlow()
    fun updateSelectedBank(bank: BankModel) {
        _selectedBank.value = bank
    }

    private val _selectedWallet = MutableStateFlow<BankModel?>(null)
    val selectedWallet = _selectedWallet.asStateFlow()
    fun updateSelectedWallet(wallet: BankModel) {
        _selectedWallet.value = wallet
    }


    fun initialize(
        accountLists: List<AccountResponseModel>?,
        argsAccountDetails: AccountResponseModel?
    ) {
        val accountIconSlugSet = accountLists?.map { it.icon }?.toSet() ?: emptySet()

        argsAccountDetails?.let { details ->
            val selectedWallet = _walletList.value.find { it.iconSlug == details.icon }
            val selectedBank = _bankList.value.find { it.iconSlug == details.icon }
            _selectedBank.value = selectedBank
            _selectedWallet.value = selectedWallet
        }

        updateBankList(_walletList.value, accountIconSlugSet)
        updateBankList(_bankList.value, accountIconSlugSet)

        if (argsAccountDetails == null) {
            val defaultBank = _bankList.value.firstOrNull { it.isEnable }
            val defaultWallet = _walletList.value.firstOrNull { it.isEnable }
            _selectedBank.value = defaultBank
            _selectedWallet.value = defaultWallet
        }
    }

    private fun updateBankList(list: List<BankModel>, accountIconSlugSet: Set<String?>) {
        list.map { item ->
            item.isEnable = item.iconSlug !in accountIconSlugSet
                    || item == _selectedBank.value
                    || item == _selectedWallet.value
        }
    }
}