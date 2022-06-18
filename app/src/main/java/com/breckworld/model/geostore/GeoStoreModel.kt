package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoStoreModel(
    val content: Content,
    val path: String,
    val type: String
) : Parcelable