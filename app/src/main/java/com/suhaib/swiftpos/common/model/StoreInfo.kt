package com.suhaib.swiftpos.common.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_info")
data class StoreInfo(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "store_id")
    val storeID: Int = 0,

    @ColumnInfo(name = "store_name")
    val storeName: String = "",

    @ColumnInfo(name = "total_sales_amount")
    val totalSalesAmount: Float = 0F,

    @ColumnInfo(name = "discount_values")
    val discountValues: Pair<Int, Int> = Pair(0, 0),

    @ColumnInfo(name = "number_of_customers")
    val numberOfCustomers: Int = 0,
)