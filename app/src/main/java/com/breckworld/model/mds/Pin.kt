package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pin(
    val active: Boolean,
    val icon: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val routable: Boolean,
    val text_bg: String,
    val text_color: String
) : Parcelable