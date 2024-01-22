package com.suhaib.swiftpos.common.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.suhaib.swiftpos.common.model.PriceBookItem
import com.suhaib.swiftpos.common.model.StoreInfo
import com.suhaib.swiftpos.common.model.StoreTransaction
import com.suhaib.swiftpos.common.model.TransactionItem
import com.suhaib.swiftpos.common.model.TransactionWithItemsAndStoreInfo
import com.suhaib.swiftpos.common.utils.AppConstants.TransactionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface SwiftDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreInfo(storeInfo: StoreInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPriceBookItemToStoreDB(priceBookItem: PriceBookItem)

    @Query("Select * from price_book ORDER BY price_book_id ASC")
    fun getAllPriceBookItems(): Flow<List<PriceBookItem>>

    @Query("SELECT * FROM store_info WHERE store_id = :storeID")
    fun getStoreInfo(storeID: Int): Flow<StoreInfo>

    @Query("SELECT * FROM price_book WHERE store_id = :storeID")
    fun getPriceBookItemsByStoreId(storeID: Int): Flow<List<PriceBookItem>>

    @Query("SELECT * FROM store_info")
    fun getAllStores(): Flow<List<StoreInfo>>

    @Upsert
    suspend fun upsertStoreTransaction(storeTransaction: StoreTransaction)

    @Upsert
    suspend fun upsertTransactionItems(transactionItemList: List<TransactionItem>)

    @Transaction
    suspend fun upsertTransactionAndTransactionItemsToDB(storeTransaction: StoreTransaction, transactionItems: List<TransactionItem>) {
        upsertStoreTransaction(storeTransaction)
        upsertTransactionItems(transactionItems)

        if (storeTransaction.transactionStatus == TransactionStatus.COMPLETED) {
            updateStoreTotalSales(storeTransaction.storeID, storeTransaction.transactionGrandTotalAmount)
            incrementStoreNumberOfCustomers(storeTransaction.storeID)
        }
    }

    @Transaction
    @Query("SELECT * FROM transaction_records WHERE store_id = :storeID AND transaction_status = :transactionStatus ORDER BY transaction_id ASC")
    fun getTransactionItemsByStoreIDAndTransactionStatus(
        storeID: Int,
        transactionStatus: TransactionStatus,
    ): Flow<List<TransactionWithItemsAndStoreInfo>>

    @Query("UPDATE store_info SET total_sales_amount = total_sales_amount + :grandTotal WHERE store_id = :storeID")
    suspend fun updateStoreTotalSales(storeID: Int, grandTotal: Float)

    @Query("UPDATE store_info SET number_of_customers = number_of_customers + 1 WHERE store_id = :storeID")
    suspend fun incrementStoreNumberOfCustomers(storeID: Int)
}