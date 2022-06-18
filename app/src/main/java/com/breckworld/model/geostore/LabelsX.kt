package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LabelsX(
    val distance: String,
    val header: String,
    val subheader: String,
    val title: String,
    val town: String
) : Parcelable