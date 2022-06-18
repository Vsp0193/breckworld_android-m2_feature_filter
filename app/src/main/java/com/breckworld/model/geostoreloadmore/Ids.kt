package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ids(
    val mds: Int,
    val slide: Int
) : Parcelable