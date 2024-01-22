package com.suhaib.swiftpos.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "price_book",
    foreignKeys = [ForeignKey(entity = StoreInfo::class, parentColumns = ["store_id"], childColumns = ["store_id"], onDelete = CASCADE)]
)
data class PriceBookItem(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("price_book_id")
    val pbID: Int,

    @ColumnInfo("item_name")
    val itemName: String,

    @ColumnInfo("tax_rates")
    val taxRates: List<Float>, // 5.5%, 6.0%

    @ColumnInfo("price")
    val price: Float,

    @ColumnInfo(name = "store_id")
    val storeID: Int,
)