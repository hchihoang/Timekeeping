package com.timekeeping.smart.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun dateToString(date: Date, format: String): String {
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        return simpleDateFormat.format(date)
    }

    fun stringToDate(dateStr: String, format: String): Date {
        val simpleDateFormat = SimpleDateFormat(
            format, Locale.getDefault()
        )
        return try {
            simpleDateFormat.parse(dateStr)
        } catch (ex: Exception) {
            Date()
        }
    }
}
