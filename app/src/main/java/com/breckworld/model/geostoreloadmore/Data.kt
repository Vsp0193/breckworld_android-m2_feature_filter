package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import com.breckworld.model.geostore.Tab
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Data(
    val tabs: List<Tab>
) : Parcelable