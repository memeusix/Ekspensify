package com.honeypot.app.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.honeypot.app.room.model.PendingTransactionModel


@Dao
interface PendingTransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPendingTransaction(pendingTransactionModel: PendingTransactionModel)

    @Query("DELETE FROM pending_transaction WHERE id = :id")
    suspend fun deletePendingTransaction(id: Int)

    @Query("SELECT * FROM pending_transaction ORDER BY created_at DESC")
    fun getPendingTransactions(): PagingSource<Int, PendingTransactionModel>

    @Query("SELECT EXISTS(SELECT 1 FROM pending_transaction WHERE notification_id = :notificationId)")
    suspend fun doesNotificationIdExist(notificationId: String): Boolean
}