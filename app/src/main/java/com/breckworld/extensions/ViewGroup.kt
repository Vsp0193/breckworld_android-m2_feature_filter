package com.breckworld.extensions

import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.inflate(
        @LayoutRes layoutResId: Int,
        attachToRoot: Boolean = false
): View = LayoutInflater.from(context).inflate(layoutResId, this, attachToRoot)