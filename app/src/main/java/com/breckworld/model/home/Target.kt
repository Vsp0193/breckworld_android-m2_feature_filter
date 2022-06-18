package com.breckworld.model.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Target(
    var mds_id: Int
) : Parcelable