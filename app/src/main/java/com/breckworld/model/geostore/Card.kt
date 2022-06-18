package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val colors: ColorsXX,
    val descriptions: Descriptions,
    val ids: Ids,
    val images: ImagesX,
    val labels: LabelsX
) : Parcelable