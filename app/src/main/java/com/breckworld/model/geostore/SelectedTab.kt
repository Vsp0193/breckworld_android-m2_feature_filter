package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedTab (
    var selectedTab: Int,
    var selectedTabIds: ArrayList<Int>,
) : Parcelable