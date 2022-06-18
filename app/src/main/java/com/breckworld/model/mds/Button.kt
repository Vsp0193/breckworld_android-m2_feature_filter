package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Button(
    var active: Boolean,
    val `data`: DataX,
    val ids: Ids,
    val images: Images,
    val labels: Labels,
    val type: String
) : Parcelable