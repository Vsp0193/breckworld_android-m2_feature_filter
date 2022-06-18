package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Labels(
    val distance: String,
    val header: String,
    val subheader: String,
    val title: String,
    val town: String
) : Parcelable