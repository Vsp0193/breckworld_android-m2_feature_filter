package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Colors(
    val footer_bg: String,
    val footer_opacity: Double,
    val footer_text: String,
    val header_bg: String,
    val header_opacity: Double,
    val header_text: String
) : Parcelable