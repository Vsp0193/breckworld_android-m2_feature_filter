package com.breckworld.utils

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel

open class BaseViewModel2 : ViewModel() {

    fun showShortMsg(context : Context, message : String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }
}