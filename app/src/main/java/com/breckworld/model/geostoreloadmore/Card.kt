package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val colors: Colors,
    val descriptions: Descriptions,
    val ids: Ids,
    val images: Images,
    val labels: Labels
) : Parcelable