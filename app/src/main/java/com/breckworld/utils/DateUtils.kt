package com.breckworld.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun changeDateFormat(calendar: Calendar, toDatePattern: String) : String {
        val formatter = SimpleDateFormat(toDatePattern, Locale.US)
        return formatter.format(calendar.time)
    }

    fun getCalendar(stringTime: String, datePattern: String): Calendar {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat(datePattern, Locale.US)
        calendar.time = sdf.parse(stringTime)
        return calendar
    }

    const val PATTERN_DATE_1: String = "yyyy-MM-dd HH:mm:ss"
    const val PATTERN_DATE_2: String = "MMMM d, yyyy"
}