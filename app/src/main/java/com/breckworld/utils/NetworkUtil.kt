package com.breckworld.util

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import com.breckworld.R

object NetworkUtil {
    /**
     * method to check active internet connection on device.
     *
     * @param context
     * @return true, if device connected to the internet. if it is false, also shows a error dialog.
     */
    fun networkStatus(context: Context?): Boolean {
        return if (context != null) {
            if (isWifiAvailable(context) || isMobileNetworkAvailable(context)) {
                true
            } else {
                Toast.makeText(context, context.resources.getString(R.string.please_check_your_internet_connection), Toast.LENGTH_SHORT).show()
                false
            }
        } else {
            false
        }
    }

    /**
     * method to check if device has enabled mobile data.
     *
     * @param context
     * @return true, if mobile data is enabled in mobile.
     */
    fun isMobileNetworkAvailable(context: Context): Boolean {
        val connectManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE && activeNetworkInfo.isConnected
    }

    /**
     * method to check device has connected with active Wifi connection.
     *
     * @param context
     * @return true, if device connected with any Wifi network.
     */
    fun isWifiAvailable(context: Context?): Boolean {
        if (context != null) {
            val connectManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI && activeNetworkInfo.isConnected
        }
        return false
    }

    fun isNetWorkAvailable(context: Context?): Boolean {
        return if (context != null) {
            isWifiAvailable(context) || isMobileNetworkAvailable(context)
        } else {
            false
        }
    }
}