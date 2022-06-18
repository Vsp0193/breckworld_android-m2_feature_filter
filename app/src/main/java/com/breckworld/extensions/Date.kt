package com.breckworld.extensions

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

fun Date.isYesterday(): Boolean = DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)

fun Date.isTomorrow(): Boolean = DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)

fun Date.isToday(): Boolean = DateUtils.isToday(this.time)

fun Date.beginOfTheDay(): Date {
    val cal = Calendar.getInstance() // locale-specific
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.time
}

fun Date.endOfTheDay(): Date {
    val cal = Calendar.getInstance() // locale-specific
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 23)
    cal.set(Calendar.MINUTE, 59)
    cal.set(Calendar.SECOND, 59)
    cal.set(Calendar.MILLISECOND, 999)
    return cal.time
}

fun Date.isSameDay(anotherDate: Date): Boolean = (this.beginOfTheDay() == anotherDate.beginOfTheDay())
