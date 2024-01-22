package com.suhaib.swiftpos.common.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_item")
data class TransactionItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("transaction_item_id")
    val id: Long = 0,

    @Embedded
    val pbItem: PriceBookItem,

    @ColumnInfo("quantity")
    var quantity: Int = 1,

    @ColumnInfo("transaction_id")
    val transactionID: Int = 0,
)