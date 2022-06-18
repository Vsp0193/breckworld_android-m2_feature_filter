package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Leaders(
    val all: List<All>,
    //val meArray: List<@RawValue Any>,
    val me: Me
) : Parcelable