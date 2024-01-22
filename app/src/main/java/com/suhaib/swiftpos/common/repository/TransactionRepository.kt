package com.suhaib.swiftpos.common.repository

import com.suhaib.swiftpos.common.db.SwiftDao
import com.suhaib.swiftpos.common.model.StoreTransaction
import com.suhaib.swiftpos.common.model.TransactionItem
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus
import javax.inject.Inject

class TransactionRepository @Inject constructor(private val swiftDao: SwiftDao) {

    suspend fun upsertTransaction(storeTransaction: StoreTransaction) {
        swiftDao.upsertStoreTransaction(storeTransaction)
    }

    suspend fun upsertTransactionItem(transactionItems: List<TransactionItem>) {
        swiftDao.upsertTransactionItems(transactionItems)
    }

    suspend fun upsertTransactionAndTransactionItemsToDB(storeTransaction: StoreTransaction, transactionItems: List<TransactionItem>) {
        swiftDao.upsertTransactionAndTransactionItemsToDB(storeTransaction, transactionItems)
    }

    fun getTransactionItemsByStoreIDAndTransactionStatus(storeID: Int, transactionStatus: TransactionStatus) =
        swiftDao.getTransactionItemsByStoreIDAndTransactionStatus(storeID, transactionStatus)
}