package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LabelsXX(
    val footer: String,
    val header: String,
    val link: String,
    val subheader: String,
    val title: String,
    val town: String,
    val distance: String
) : Parcelable