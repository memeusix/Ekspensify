package com.honeypot.app.ui.autoTracking.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.honeypot.app.room.repository.PendingTransactionRepo
import com.honeypot.app.utils.smsReceiver.SmsHelper
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AutoTrackViewModel @Inject constructor(
    @ApplicationContext val context: Context,
    val spUtilsManager: SpUtilsManager,
    private val pendingTransactionRepo: PendingTransactionRepo
) : ViewModel() {

    private val _isSmsFeatureEnable =
        MutableStateFlow<Boolean>(spUtilsManager.isAutoTrackingEnable.value)
    val isSmsFeatureEnable = _isSmsFeatureEnable.asStateFlow()

    fun toggleSmsFeature(enable: Boolean) {

        if (enable) {
            if (!SmsHelper.isNotificationAccessEnable(context)) {
                SmsHelper.requestNotificationAccess(context)
                return
            }
        }
        _isSmsFeatureEnable.value = enable
        spUtilsManager.updateAutoTracking(enable)
//        updateReceiverState(context, enable)
    }

}

