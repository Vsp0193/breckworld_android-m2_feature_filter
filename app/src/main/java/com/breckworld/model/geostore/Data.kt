package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val banners: List<Banner>,
    val colors: ColorsX,
    val filters: List<Filter>,
    val images: Images,
    val labels: Labels,
    val sort: List<Sort>,
    var tabs: List<Tab>
) : Parcelable