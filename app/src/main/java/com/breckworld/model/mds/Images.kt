package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Images(
    val icon: String,
    val icon_active: String
) : Parcelable