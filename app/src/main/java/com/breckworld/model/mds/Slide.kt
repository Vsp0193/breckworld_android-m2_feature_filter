package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Slide(
    val colors: Colors,
    val descriptions: Descriptions,
    val ids: IdsXXX,
    val images: ImagesXX,
    val labels: LabelsXX
) : Parcelable