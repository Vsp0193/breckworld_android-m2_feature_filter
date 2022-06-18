package com.breckworld.ui.main.offerPreviewDialog

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.OfferDB
import com.breckworld.repository.remote.http.model.RedeemRequest
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class OfferPreviewViewModel : BaseMVVMViewModel<OfferPreviewFragment.Events>() {

    var offerDB: OfferDB? = null
    val title = MutableLiveData<String>()
    val photoUrl = MutableLiveData<String>()
    val description = MutableLiveData<String>()
    val placeholderId = MutableLiveData<Int>()
    val isClaimCouponBtnEnabled = MediatorLiveData<Boolean>()
    val adults = MutableLiveData<Int>()
    val children = MutableLiveData<Int>()

    init {
        val observer = Observer<Int> {
            isClaimCouponBtnEnabled.value = isDataChanged()
        }
        isClaimCouponBtnEnabled.addSource(adults, observer)
        isClaimCouponBtnEnabled.addSource(children, observer)
    }

    fun initData(offerDB: OfferDB) {
        this.offerDB = offerDB
        title.value = offerDB.title
        photoUrl.value = offerDB.image
        description.value = offerDB.description
        placeholderId.value = R.drawable.ic_town
    }

    private fun isDataChanged(): Boolean {
        return (adults.value != null && adults.value!! > 0) ||
                (children.value != null && children.value!! > 0)
    }

    fun onClaimCoupon() {
        showLoading()
        launchCoroutine({
            val response = repository.redeem(
                offerDB?.entryId!!, RedeemRequest(offerDB?.pickLat, offerDB?.pickLng, adults.value, children.value)
            ).await()
            hideLoading()
            if (response.errorMessage == null) {
                offerDB?.redeemed = 1
                withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                    repository.updateOfferDB(offerDB!!)
                }
                _eventsLiveData.value = Event(OfferPreviewFragment.Events.BACK)
            } else {
                showError(response.errorMessage)
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }
}