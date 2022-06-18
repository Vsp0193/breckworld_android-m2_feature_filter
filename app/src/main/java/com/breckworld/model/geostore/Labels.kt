package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Labels(
    val no_results: String
) : Parcelable