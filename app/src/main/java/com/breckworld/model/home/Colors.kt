package com.breckworld.model.home

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Colors(
    var banner_bg: String,
    var banner_text: String
) : Parcelable