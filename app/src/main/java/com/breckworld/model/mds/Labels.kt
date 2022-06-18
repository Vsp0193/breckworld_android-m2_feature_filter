package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Labels(
    val title: String
) : Parcelable