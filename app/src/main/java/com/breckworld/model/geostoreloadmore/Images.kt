package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Images(
    val footer: String,
    val icon: String,
    val logo: String,
    val slide_card: String
) : Parcelable