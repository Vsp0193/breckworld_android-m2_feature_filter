package com.breckworld.ui.main.starsTab.starsClues

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.remote.http.model.LocationStar
import  com.breckworld.databinding.ItemStarClueBinding
import com.breckworld.repository.database.model.LocationStarDB

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 23:06
 *         E-mail: bondes87@gmail.com
 */
class StarsClueRecyclerAdapter (list: MutableList<LocationStarDB>,
                                listener: BaseRecyclerViewAdapter.Listener<LocationStarDB>)
    : BaseRecyclerViewAdapter<StarClueItemState,ItemStarClueBinding, LocationStarDB>(list, listener) {

    override fun itemState(item: LocationStarDB, listener: BaseRecyclerViewAdapter.Listener<LocationStarDB>): StarClueItemState = StarClueItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_star_clue
}