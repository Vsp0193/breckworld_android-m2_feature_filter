package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Colors(
    val banner_bg: String,
    val banner_text: String
) : Parcelable