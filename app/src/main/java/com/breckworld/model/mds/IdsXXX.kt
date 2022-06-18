package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdsXXX(
    val linked_mds: Int? = null,
    val slide: Int
) : Parcelable