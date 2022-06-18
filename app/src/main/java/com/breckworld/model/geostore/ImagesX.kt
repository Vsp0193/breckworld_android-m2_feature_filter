package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesX(
    val footer: String,
    val icon: String,
    val logo: String,
    val slide_card: String
) : Parcelable