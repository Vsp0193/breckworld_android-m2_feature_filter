package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Ctabs(
    val align: String,
    val colors: ColorsX,
    val icon: String,
    val id: Int,
    val label: String,
    val type: String
) :Parcelable