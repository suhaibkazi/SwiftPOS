package com.suhaib.swiftpos.common.utils

import java.math.BigDecimal

inline fun <reified T : Number> T.getNDecimalPoint(point: Int): T {
    val formattedValue = String.format("%.${point}f", this.toDouble())
    return when (this) {
        is Double -> formattedValue.toDouble() as T
        is Float -> formattedValue.toFloat() as T
        is BigDecimal -> BigDecimal(formattedValue) as T
        else -> throw IllegalArgumentException("Unsupported number type")
    }
}