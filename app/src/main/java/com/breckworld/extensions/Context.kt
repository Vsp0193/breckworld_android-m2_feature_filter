package com.breckworld.extensions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.DisplayMetrics
import android.widget.Toast
import com.breckworld.BuildConfig

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.toast(resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

fun Context.convertDpToPixel(dp: Int): Int {
    val metrics = resources.displayMetrics
    return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}

inline fun Context.debug(code: () -> Unit) {
    if (BuildConfig.DEBUG) {
        code()
    }
}

fun Context.hasNetworkConnection(): Boolean {
    val activeNetworkInfo = getConnectivityManager().activeNetworkInfo
    return activeNetworkInfo != null && activeNetworkInfo.isConnected
}

fun Context.intentSafe(intent: Intent): Boolean {
    val activities = packageManager
            .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
    return activities.isNotEmpty()
}