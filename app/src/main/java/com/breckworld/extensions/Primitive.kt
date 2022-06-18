package com.breckworld.extensions

import android.content.res.Resources
import android.util.TypedValue

fun Int.toDp() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics)

fun Float.toPx() = Math.round(this.toInt() * Resources.getSystem().displayMetrics.density)
fun Int.toPx() = Math.round(this.toInt() * Resources.getSystem().displayMetrics.density)

fun Boolean?.falseIfNull() = this ?: false
