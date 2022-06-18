package com.breckworld.model.mds

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Me(
    val name: String,
    val position: Int
) : Parcelable