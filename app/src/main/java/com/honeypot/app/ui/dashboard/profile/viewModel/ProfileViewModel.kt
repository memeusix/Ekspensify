package com.honeypot.app.ui.dashboard.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.BaseViewModel
import com.honeypot.app.data.model.responseModel.UserResponseModel
import com.honeypot.app.data.repository.ProfileRepository
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    val spUtilsManager: SpUtilsManager
) : ViewModel(), BaseViewModel {

    /**
     * Get Profile Details
     */
    private val _getMe = MutableStateFlow<ApiResponse<UserResponseModel>>(ApiResponse.idle())
    val getMe = _getMe.asStateFlow()

    init {
        fetchUserInfo()
    }

    // fetching use info from catching and if null then from server
    private fun fetchUserInfo() {
        spUtilsManager.user.value?.let {
            _getMe.value = ApiResponse.success(it)
        } ?: run {
            getMe()
        }
    }

    fun getMe() {
        viewModelScope.launch {
            _getMe.value = ApiResponse.loading(
                _getMe.value.data,
            )
            val response = handleData(_getMe.value) { profileRepository.getMe() }
            _getMe.value = response
            if (response is ApiResponse.Success) {
                spUtilsManager.updateUser(response.data)
            }
        }
    }

    /**
     * Update me
     */
    private val _updateMe = MutableStateFlow<ApiResponse<UserResponseModel>>(ApiResponse.idle())
    val updateMe = _updateMe.asStateFlow()

    fun updateMe(user: UserResponseModel) {
        viewModelScope.launch {
            _updateMe.value = ApiResponse.loading(
                _updateMe.value.data,
            )
            val response = profileRepository.updateMe(user)
            _updateMe.value = response
            if (response is ApiResponse.Success) {
                spUtilsManager.updateUser(response.data)
            }
            _getMe.value = response
        }
    }

}