package com.breckworld.ui.main.wallet

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.database.model.OfferDB
import com.breckworld.databinding.ItemWalletBinding

/**
 * @author Dmytro Bondarenko
 *         Date: 07.06.2019
 *         Time: 12:21
 *         E-mail: bondes87@gmail.com
 */
class WalletsRecyclerAdapter (list: MutableList<OfferDB>,
                              listener: BaseRecyclerViewAdapter.Listener<OfferDB>)
    : BaseRecyclerViewAdapter<WalletItemState, ItemWalletBinding, OfferDB>(list, listener) {

    override fun itemState(item: OfferDB, listener: BaseRecyclerViewAdapter.Listener<OfferDB>): WalletItemState = WalletItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_wallet
}