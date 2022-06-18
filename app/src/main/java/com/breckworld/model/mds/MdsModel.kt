package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MdsModel(
    val `data`: Data,
    val type: String
) : Parcelable