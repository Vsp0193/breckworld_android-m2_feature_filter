package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImagesXX(
    val logo: String,
    val slide_card: String,
    val slide_full: String,
    val icon: String,
    val footer: String,
) : Parcelable