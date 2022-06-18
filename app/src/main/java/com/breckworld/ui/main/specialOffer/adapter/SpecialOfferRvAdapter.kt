package com.breckworld.ui.main.specialOffer.adapter

import androidx.databinding.ViewDataBinding
import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.database.model.SpecialOfferDB

class SpecialOfferRvAdapter(list: MutableList<SpecialOfferDB>, listener: Listener<SpecialOfferDB>)
    : BaseRecyclerViewAdapter<SpecialOfferItemState, ViewDataBinding, SpecialOfferDB>(list, listener) {

    override fun layoutResId(): Int = R.layout.item_special_offer

    override fun itemState(item: SpecialOfferDB, listener: BaseRecyclerViewAdapter.Listener<SpecialOfferDB>): SpecialOfferItemState =
        SpecialOfferItemState(item, listener as Listener<SpecialOfferDB>)

    interface Listener<I> : BaseRecyclerViewAdapter.Listener<I>

}