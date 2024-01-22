package com.suhaib.swiftpos.common.model

import androidx.room.Embedded
import androidx.room.Relation

data class TransactionWithItemsAndStoreInfo(
    @Embedded
    val storeTransaction: StoreTransaction,

    @Relation(parentColumn = "transaction_id", entityColumn = "transaction_id")
    val transactionItems: List<TransactionItem>,

    @Relation(parentColumn = "store_id", entityColumn = "store_id")
    val storeInfo: StoreInfo
)