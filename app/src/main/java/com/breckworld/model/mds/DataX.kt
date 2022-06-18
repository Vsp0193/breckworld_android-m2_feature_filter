package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class DataX(
    val html: String,
    val leaders: Leaders,
    val pins: List<Pin>,
    val slides: List<@RawValue Any>,
    val url: String
): Parcelable