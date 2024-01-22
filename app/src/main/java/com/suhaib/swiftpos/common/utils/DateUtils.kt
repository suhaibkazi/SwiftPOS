package com.suhaib.swiftpos.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    fun convertEpochToDateTime(epoch: Long): String = try {
        SimpleDateFormat("MMM d, yyyy hh:mm a", Locale.ENGLISH).format(Date(epoch))
    } catch (e: Exception) {
        "Invalid timestamp"
    }

    fun convertEpochToDateTimeFormatted(epoch: Long): String = try {
        SimpleDateFormat("dd MMM yyyy hh:mm:ss a", Locale.ENGLISH).format(Date(epoch))
    } catch (e: Exception) {
        "Invalid timestamp"
    }


}