package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Banner(
    val colors: Colorss,
    val icon: String,
    val label: String,
    val target: Target
) : Parcelable