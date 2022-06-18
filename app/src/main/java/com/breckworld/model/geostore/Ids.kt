package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ids(
    val mds: Int,
    val slide: Int
) : Parcelable