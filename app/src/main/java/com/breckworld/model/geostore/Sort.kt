package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sort(
    val id: String,
    val name: String,
    var active: Boolean
) : Parcelable