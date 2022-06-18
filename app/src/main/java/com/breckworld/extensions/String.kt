package com.breckworld.extensions

import android.util.Patterns

fun String?.checkLength(length: Int): Boolean {
    if (this == null) {
        return false
    }
    return this.length >= length
}

fun String?.isValidEmail(): Boolean {
    if (this == null) {
        return false
    }
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String?.nonesym(): String? {
    if (this == null) return null
    if (this.toLowerCase() == "none") return null
    return this
}