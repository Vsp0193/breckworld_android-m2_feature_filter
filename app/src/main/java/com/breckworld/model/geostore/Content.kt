package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Content(
    val `data`: Data,
    val type: String
) : Parcelable