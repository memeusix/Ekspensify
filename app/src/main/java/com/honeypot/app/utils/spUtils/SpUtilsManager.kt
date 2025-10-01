package com.honeypot.app.utils.spUtils

import com.honeypot.app.data.model.responseModel.AccountListModel
import com.honeypot.app.data.model.responseModel.CategoryListModel
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.ui.theme.Theme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpUtilsManager @Inject constructor(
    private val spUtils: SpUtils
) {

    private val _themePreference = MutableStateFlow(spUtils.themePreference)
    val themePreference: StateFlow<String> get() = _themePreference
    fun toggleTheme() {
        val currentTheme = Theme.valueOf(themePreference.value)
        val newTheme = currentTheme.next()
        _themePreference.value = newTheme.name
        spUtils.themePreference = newTheme.name
    }

    private val _isLoggedIn = MutableStateFlow(spUtils.isLoggedIn)
    val isLoggedIn: StateFlow<Boolean> get() = _isLoggedIn
    fun updateLoginStatus(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
        spUtils.isLoggedIn = isLoggedIn
    }

    private val _isAutoTrackingEnable = MutableStateFlow(spUtils.isAutoTrackingEnable)
    val isAutoTrackingEnable: StateFlow<Boolean> get() = _isAutoTrackingEnable
    fun updateAutoTracking(isAutoTrackingEnable: Boolean) {
        _isAutoTrackingEnable.value = isAutoTrackingEnable
        spUtils.isAutoTrackingEnable = isAutoTrackingEnable
    }

    private val _accessToken = MutableStateFlow(spUtils.accessToken)
    val accessToken: StateFlow<String> get() = _accessToken
    fun updateAccessToken(accessToken: String) {
        _accessToken.value = accessToken
        spUtils.accessToken = accessToken
    }

    private val _user = MutableStateFlow(spUtils.user)
    val user: StateFlow<UserResponseModel?> get() = _user
    fun updateUser(user: UserResponseModel?) {
        _user.value = user
        spUtils.user = user
    }

    private val _accountData = MutableStateFlow(spUtils.accountData)
    val accountData: StateFlow<AccountListModel?> get() = _accountData
    fun updateAccountData(accountData: AccountListModel?) {
        _accountData.value = accountData
        spUtils.accountData = accountData
    }

    private val _categoriesData = MutableStateFlow(spUtils.categoriesData)
    val categoriesData: StateFlow<CategoryListModel?> get() = _categoriesData
    fun updateCategoriesData(categoriesData: CategoryListModel?) {
        _categoriesData.value = categoriesData
        spUtils.categoriesData = categoriesData
    }

    fun logout() {
        spUtils.logout()
        _themePreference.value = Theme.LIGHT.name
        _isLoggedIn.value = false
        _accessToken.value = ""
        _user.value = null
        _accountData.value = null
        _categoriesData.value = null
    }
}