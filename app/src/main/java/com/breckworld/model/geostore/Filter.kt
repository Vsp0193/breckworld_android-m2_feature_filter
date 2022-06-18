package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Filter(
    var default: Int,
    val items: List<Item>,
    val title: String
) : Parcelable