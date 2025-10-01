package com.honeypot.app.ui.export.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.honeypot.app.data.ApiResponse
import com.honeypot.app.data.model.requestModel.ExportRequestModel
import com.honeypot.app.data.repository.ExportRepository
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExportViewModel @Inject constructor(
    spUtilsManager: SpUtilsManager,
    private val exportRepository: ExportRepository
) : ViewModel() {

    // useDetails
    val user = spUtilsManager.user.value

    // send Request

    private val _requestStatements = MutableStateFlow<ApiResponse<Any>>(ApiResponse.idle())
    val requestStatements = _requestStatements.asStateFlow()
    fun requestStatements(exportRequestModel: ExportRequestModel) {
        viewModelScope.launch {
            _requestStatements.value = ApiResponse.loading()
            _requestStatements.value = exportRepository.requestStatements(exportRequestModel)

            delay(500)
            _requestStatements.value = ApiResponse.idle()
        }
    }
}