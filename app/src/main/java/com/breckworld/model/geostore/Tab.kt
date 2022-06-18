package com.breckworld.model.geostore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tab(
    var cards: List<Card>,
    val id: String,
    val load_more: String,
    var tab_count: Int,
    val title: String,
    var isSelected: Boolean,
    var selectedSorting: String = "",
    var selectedFilterIds: ArrayList<Int>,
) : Parcelable