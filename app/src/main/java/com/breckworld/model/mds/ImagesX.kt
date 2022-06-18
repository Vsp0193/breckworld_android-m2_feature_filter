package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesX(
    val icon: String,
    val icon_active: String
) : Parcelable