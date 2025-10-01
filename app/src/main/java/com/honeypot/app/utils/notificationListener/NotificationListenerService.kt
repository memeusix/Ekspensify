package com.honeypot.app.utils.notificationListener

import android.app.Notification
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.honeypot.app.room.repository.PendingTransactionRepo
import com.honeypot.app.utils.smsReceiver.SmsHelper
import com.honeypot.app.utils.spUtils.SpUtilsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListenerService : NotificationListenerService() {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var pendingTransactionRepo: PendingTransactionRepo

    @Inject
    lateinit var spUtilsManager: SpUtilsManager

    companion object {
        private const val TAG = "NotificationListener"
    }
//
//    override fun onCreate() {
//        super.onCreate()
//        try {
//            val db = HoneyPotDatabase.getDataBase(applicationContext)
//            pendingTransactionRepo = PendingTransactionRepo(db.pendingTransactionDao())
//        } catch (e: Exception) {
//            Log.e(TAG, "Error in onCreate: ${e.message}")
//        }
//    }

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return

        if (spUtilsManager.isAutoTrackingEnable.value) {
            processNotification(sbn)
        }
    }

    private fun processNotification(sbn: StatusBarNotification) {
        val notificationId = sbn.postTime.toString()
        val extras = sbn.notification.extras ?: return

        val title = extras.getString(Notification.EXTRA_TITLE)
        val text = extras.getString(Notification.EXTRA_TEXT) ?: return

        // Avoid println in production - use Log instead
        Log.d(TAG, "Message: $text")
        Log.d(TAG, "Notification Id: $notificationId")

        scope.launch {
            try {
                val pendingTransactionModel = SmsHelper.parseSms(text)?.copy(
                    notificationId = notificationId
                ) ?: return@launch

                Log.d(TAG, "Pending Model: $pendingTransactionModel")

                if (!pendingTransactionRepo.doesNotificationIdExist(notificationId)) {
                    pendingTransactionRepo.createPendingTransaction(pendingTransactionModel)
                        .fold(
                            onSuccess = {
                                Log.d(TAG, "Transaction Added with ID: ${it.id}")
                            },
                            onFailure = { error ->
                                Log.e(TAG, "Transaction Failed: ${error.message}")
                            }
                        )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing notification: ${e.message}")
            }
        }
    }


    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        super.onNotificationRemoved(sbn)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}