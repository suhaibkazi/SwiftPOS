package com.suhaib.swiftpos.common.db

import androidx.room.TypeConverter
import com.suhaib.swiftpos.common.model.TransactionItem
import com.suhaib.swiftpos.common.utils.toDataClassObject
import com.suhaib.swiftpos.common.utils.toDataClassString

class SwiftTypeConvertors {
    @TypeConverter
    fun fromListOfFloats(list: List<Float>): String =
        list.joinToString(separator = ";") { it.toString() }

    @TypeConverter
    fun toListOfFloats(string: String?): List<Float> =
        ArrayList(string?.split(";")?.mapNotNull { it.toFloatOrNull() } ?: emptyList())

    @TypeConverter
    fun convertTransactionItemsToJSONString(transactionItem: List<TransactionItem>): String = transactionItem.toDataClassString()

    @TypeConverter
    fun convertJSONStringToTransactionItems(transactionString: String): List<TransactionItem> = transactionString.toDataClassObject()

    @TypeConverter
    fun fromPair(pair: Pair<Int, Int>): String {
        return "${pair.first},${pair.second}"
    }

    @TypeConverter
    fun toPair(stringValue: String): Pair<Int, Int> {
        val (first, second) = stringValue.split(",").map { it.toInt() }
        return Pair(first, second)
    }
}