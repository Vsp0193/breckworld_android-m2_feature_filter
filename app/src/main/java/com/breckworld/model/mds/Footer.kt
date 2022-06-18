package com.breckworld.model.mds

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Footer(
    val active: Boolean,
    val data: DataXX,
    val ids: IdsX,
    val images: ImagesX,
    val labels: LabelsX,
    val type: String
) : Parcelable