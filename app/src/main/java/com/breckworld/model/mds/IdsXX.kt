package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class IdsXX(
    val active_slide: Int,
    val mds: Int
) : Parcelable