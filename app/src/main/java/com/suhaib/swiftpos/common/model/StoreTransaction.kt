package com.suhaib.swiftpos.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus

@Entity(tableName = "transaction_records")
data class StoreTransaction(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "transaction_id")
    val transactionID: Int = 0,

    @ColumnInfo(name = "transaction_start_time")
    val transactionStartTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "transaction_end_time")
    val transactionEndTime: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "transaction_sub_total_amount")
    val transactionSubTotalAmount: Float = 0F,

    @ColumnInfo(name = "transaction_total_discount_amount")
    val transactionTotalDiscountAmount: Float = 0F,

    @ColumnInfo(name = "transaction_total_tax_amount")
    val transactionTotalTaxAmount: Float = 0F,

    @ColumnInfo(name = "transaction_grand_total_amount")
    val transactionGrandTotalAmount: Float = 0F,

    @ColumnInfo(name = "transaction_status")
    val transactionStatus: TransactionStatus = TransactionStatus.SAVED,

    @ColumnInfo(name = "store_id")
    val storeID: Int = 0,
)