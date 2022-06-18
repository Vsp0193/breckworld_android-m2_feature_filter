package com.breckworld.ui.main.search

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.ItemCityBinding
import com.breckworld.repository.local.model.CityModel

/**
 * @author Dmytro Bondarenko
 *         Date: 04.06.2019
 *         Time: 16:21
 *         E-mail: bondes87@gmail.com
 */
class CitiesRecyclerAdapter(
    list: MutableList<CityModel>, private var selectPosition: Int,
    listener: BaseRecyclerViewAdapter.Listener<CityModel>
) : BaseRecyclerViewAdapter<CityItemState, ItemCityBinding, CityModel>(list, listener) {

    override fun itemState(item: CityModel, listener: BaseRecyclerViewAdapter.Listener<CityModel>): CityItemState =
        CityItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_city

    override fun onBindViewHolder(holder: BaseViewHolder<ItemCityBinding>, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isNotEmpty()) {
            holder.binding.imageViewPhoto.isSelected = selectPosition == position
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ItemCityBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.binding.imageViewPhoto.isSelected = selectPosition == position
    }

    fun changeSelected(position: Int) {
        val oldSelectedPosition = selectPosition
        selectPosition = position
        notifyItemChanged(oldSelectedPosition, oldSelectedPosition)
        notifyItemChanged(selectPosition, selectPosition)
    }
}