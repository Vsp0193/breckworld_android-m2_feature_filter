package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    val icon: String,
    val id: Int,
    val title: String,
    var isSelected: Boolean
) : Parcelable