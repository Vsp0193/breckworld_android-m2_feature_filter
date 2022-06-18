package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorsX(
    val header_bg: String,
    val header_opacity: Double,
    val tab_bg: String,
    val tab_bg_active: String,
    val tab_border: String,
    val tab_opacity: Double,
    val tab_border_opacity: Double,
    val tab_text: String,
    val tab_text_active: String
) : Parcelable