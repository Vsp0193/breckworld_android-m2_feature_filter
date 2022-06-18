package com.breckworld.ui.main.wallet

import androidx.lifecycle.MutableLiveData
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.database.model.OfferDB

/**
 * @author Dmytro Bondarenko
 *         Date: 07.06.2019
 *         Time: 12:21
 *         E-mail: bondes87@gmail.com
 */
class WalletItemState(
    private val item: OfferDB,
    private val listener: BaseRecyclerViewAdapter.Listener<OfferDB>
) : BaseItemState() {

    var title = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var photoUrl = MutableLiveData<String>()
    var placeholderId = MutableLiveData<Int>()
    var radius = MutableLiveData<Int>()
    var redeemed = MutableLiveData<Boolean>()
    var alpha = MutableLiveData<Float>()

    init {
        title.value = item.title
        description.value = if (item.description.isBlank()) {
            App.getStringFromRes(R.string.no_description)
        } else {
            item.description
        }
        photoUrl.value = item.image
        redeemed.value = item.redeemed == 1
        placeholderId.value = R.drawable.ic_offer_placeholder
        radius.value = App.getResDimensionPixelSize(R.dimen.photo_corner)
        if (redeemed.value!!){
            alpha.value = 0.5f
        } else{
            alpha.value = 1.0f
        }
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}