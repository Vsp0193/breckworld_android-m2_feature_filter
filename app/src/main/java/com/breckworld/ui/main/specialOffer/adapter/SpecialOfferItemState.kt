package com.breckworld.ui.main.specialOffer.adapter

import androidx.databinding.ObservableField
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseItemState
import com.breckworld.repository.database.model.SpecialOfferDB

class SpecialOfferItemState(
    private val item: SpecialOfferDB,
    private val listener: SpecialOfferRvAdapter.Listener<SpecialOfferDB>
) : BaseItemState() {
    val title = ObservableField<String>()
    val description = ObservableField<String>()
    val image = ObservableField<String>()
    val placeholderId = ObservableField<Int>()
    val radius = ObservableField<Int>()

    init {
        title.set(item.title)
        description.set(item.description)
        image.set(item.image)
        placeholderId.set(R.drawable.ic_offer)
        radius.set(App.getResDimensionPixelSize(R.dimen.photo_corner))
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}