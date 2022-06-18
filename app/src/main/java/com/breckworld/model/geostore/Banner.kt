package com.breckworld.model.geostore

import android.os.Parcelable
import com.breckworld.model.home.Colors
import com.breckworld.model.home.Target
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Banner(
    val colors: Colors,
    val icon: String,
    val label: String,
    val target: Target
) : Parcelable