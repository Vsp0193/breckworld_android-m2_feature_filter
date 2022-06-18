package com.breckworld.extensions

import android.widget.Toast

fun androidx.fragment.app.Fragment.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    activity?.toast(text, duration)
}

fun androidx.fragment.app.Fragment.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    activity?.toast(resId, duration)
}