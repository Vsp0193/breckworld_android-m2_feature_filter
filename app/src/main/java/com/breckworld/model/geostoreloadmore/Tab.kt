package com.breckworld.model.geostoreloadmore

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Tab(
    val cards: List<Card>,
    val id: String,
    val load_more: String,
    val tab_count: String,
    val title: String,
    var isSelected: Boolean
) : Parcelable