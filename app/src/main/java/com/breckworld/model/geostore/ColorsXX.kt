package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorsXX(
    val footer_bg: String,
    val footer_opacity: Int,
    val footer_text: String,
    val header_bg: String,
    val header_opacity: Int,
    val header_text: String
) : Parcelable