package com.honeypot.app.utils.smsReceiver

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.honeypot.app.room.database.HoneyPotDatabase
import com.honeypot.app.room.repository.PendingTransactionRepo


/**
 * We removed all SMS permissions and the SMS Receiver.
 * Instead, we are using the Notification Service to track SMS transactions.
 *
 * For more details, @see /utils/notificationListener/NotificationListenerService.kt
 */

class SmsProcessingWorker(
    appContext: Context,
    workParam: WorkerParameters,
) : CoroutineWorker(appContext, workParam) {


    override suspend fun doWork(): Result {

        val smsBody = inputData.getString("sms_body") ?: return Result.failure()

        val processTransactionData = SmsHelper.parseSms(smsBody) ?: return Result.failure()


        val honeyPotDatabase = HoneyPotDatabase.getDataBase(applicationContext)
        val pendingTransactionDao = honeyPotDatabase.pendingTransactionDao()
        val pendingTransactionRepo = PendingTransactionRepo(pendingTransactionDao)

        val response = pendingTransactionRepo.createPendingTransaction(processTransactionData)

        response.fold(
            onSuccess = {
                return Result.success()
            },
            onFailure = {
                return Result.failure()
            }
        )
    }

    companion object {
        private val TAG = "SmsProcessingWorker"
    }

}
