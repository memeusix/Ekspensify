package com.memeusix.budgetbuddy.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.memeusix.budgetbuddy.utils.TransactionType
import java.time.LocalDate

@Entity(
    tableName = "pending_transaction"
)
data class PendingTransactionModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "amount")
    val amount: Int? = null,

    @ColumnInfo(name = "type")
    val type: TransactionType? = null,

    @ColumnInfo(name = "account_name")
    val accountName: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,
    
    @ColumnInfo(name = "created_at")
    val createdAt: String? = null
)