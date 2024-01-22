package com.suhaib.swiftpos.common.repository

import com.suhaib.swiftpos.common.db.SwiftDao
import com.suhaib.swiftpos.common.model.PriceBookItem
import com.suhaib.swiftpos.common.model.StoreInfo
import kotlinx.coroutines.flow.Flow

import javax.inject.Inject

class StoreRepository @Inject constructor(private val swiftDao: SwiftDao) {

    val allStores: Flow<List<StoreInfo>> = swiftDao.getAllStores()

    fun getPriceBookItemsByStoreID(storeID: Int): Flow<List<PriceBookItem>> =
        swiftDao.getPriceBookItemsByStoreId(storeID)

    fun getStoreInfoWithPriceBookItems(storeID: Int) = swiftDao.getStoreInfo(storeID)

    suspend fun insertStoreInfo(storeInfo: StoreInfo) {
        swiftDao.insertStoreInfo(storeInfo)
    }

    suspend fun insertPriceBookItem(priceBookItem: PriceBookItem) {
        swiftDao.insertPriceBookItemToStoreDB(priceBookItem)
    }
}