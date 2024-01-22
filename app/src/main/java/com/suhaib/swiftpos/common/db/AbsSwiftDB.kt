package com.suhaib.swiftpos.common.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.suhaib.swiftpos.common.model.PriceBookItem
import com.suhaib.swiftpos.common.model.StoreInfo
import com.suhaib.swiftpos.common.model.StoreTransaction
import com.suhaib.swiftpos.common.model.TransactionItem

@Database(
    entities = [PriceBookItem::class, StoreTransaction::class, TransactionItem::class, StoreInfo::class], version = 1, exportSchema =
    false
)
@TypeConverters(SwiftTypeConvertors::class)
abstract class AbsSwiftDB : RoomDatabase() {
    abstract fun swiftDao(): SwiftDao
}