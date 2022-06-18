package com.breckworld.ui.main.specialOffer

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.repository.database.mapper.SpecialOffersMapper
import com.breckworld.repository.database.model.SpecialOfferDB
import com.breckworld.repository.database.model.TableCache
import com.breckworld.repository.database.model.TableNames
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class SpecialOfferViewModel : BaseMVVMViewModel<SpecialOfferFragment.Events>() {

    val specialOffers = repository.getSpecialOffersDBLiveData()
    val isLoading = MutableLiveData<Boolean>()
    var location: Location? = null

    fun checkAndUpdateSpecialOffers(isRefresh: Boolean) {
        launchCoroutine({
            val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.getTableCacheData(TableNames.SpecialOffer)
            }
            val lastData: TableCache =
                if (data.isEmpty()) TableCache(TableNames.SpecialOffer, 0, 0.0, 0.0, true) else data[0]
            val needUpdate = System.currentTimeMillis() - lastData.lastUpdate > Constants.DATABASE_UPDATE_INTERVAL

            if (needUpdate || isRefresh) {
                isLoading.value = true && !isRefresh
                showLoading()
                val response = repository.getSpecialOffers().await()
                lastData.needUpdate = true
                withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                    repository.insertTableCacheData(lastData)
                    repository.updateSpecialOffersDB(SpecialOffersMapper().map(response))
                }
                hideLoading()
            } else {
                hideLoading()
                isLoading.value = false
            }
        }, { _, throwable ->
            hideLoading()
            isLoading.value = false
            showError(throwable.message)
        })
    }

    fun collectSpecialOffer(specialOfferDB: SpecialOfferDB) {
        isLoading.value = true
        launchCoroutine({
            val response = repository.collectOffer(
                offerId = specialOfferDB.id,
                lat = location?.latitude ?: 0.0,
                lng = location?.longitude ?: 0.0
            ).await()
            if (response.success == 1) {
                onSpecialOfferCollected()
            } else if (response.error == 1) {
                showError(response.errorMessage)
            }
            isLoading.value = false
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    private fun onSpecialOfferCollected() {
        _eventsLiveData.value = Event(SpecialOfferFragment.Events.SPECIAL_OFFER_COLLECTED)
    }

    fun onBack() {
        _eventsLiveData.value = Event(SpecialOfferFragment.Events.BACK)
    }
}