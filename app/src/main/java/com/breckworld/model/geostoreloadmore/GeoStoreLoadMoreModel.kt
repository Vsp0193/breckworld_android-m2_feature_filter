package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoStoreLoadMoreModel(
    val content: Content,
    val path: String,
    val type: String
) : Parcelable