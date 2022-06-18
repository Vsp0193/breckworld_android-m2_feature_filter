package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Colorss(
    val banner_bg: String,
    val banner_text: String
) : Parcelable