package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ColorsX(
    val banner_bg: String,
    val banner_text: String
) : Parcelable