package com.breckworld.ui.main.starsTab.collectedStars

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.ItemCollectedStarBinding
import com.breckworld.repository.database.model.StarDB

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 23:06
 *         E-mail: bondes87@gmail.com
 */
class CollectedStarsRecyclerAdapter(
    list: MutableList<StarDB>,
    listener: BaseRecyclerViewAdapter.Listener<StarDB>
) : BaseRecyclerViewAdapter<CollectedStarItemState, ItemCollectedStarBinding, StarDB>(list, listener) {

    override fun itemState(item: StarDB, listener: BaseRecyclerViewAdapter.Listener<StarDB>): CollectedStarItemState =
        CollectedStarItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_collected_star
}