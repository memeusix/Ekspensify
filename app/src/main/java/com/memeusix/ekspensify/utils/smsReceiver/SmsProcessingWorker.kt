package com.memeusix.ekspensify.utils.smsReceiver

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.memeusix.ekspensify.room.database.EkspensifyDatabase
import com.memeusix.ekspensify.room.repository.PendingTransactionRepo

class SmsProcessingWorker(
    appContext: Context,
    workParam: WorkerParameters,
) : CoroutineWorker(appContext, workParam) {


    override suspend fun doWork(): Result {

        val smsBody = inputData.getString("sms_body") ?: return Result.failure()

        val processTransactionData = SmsHelper.parseSms(smsBody) ?: return Result.failure()


        val ekspensifyDataBase = EkspensifyDatabase.getDataBase(applicationContext)
        val pendingTransactionDao = ekspensifyDataBase.pendingTransactionDao()
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
