package com.breckworld.ui.main.offerdialog

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.repository.database.mapper.OffersMapper
import com.breckworld.repository.database.mapper.ProfileMapper
import com.breckworld.repository.database.mapper.StarsMapper
import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.database.model.SpecialOfferDB
import com.breckworld.repository.database.model.TableCache
import com.breckworld.repository.database.model.TableNames
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.Serializable

class OfferDialogViewModel : BaseMVVMViewModel<OfferDialogFragment.Events>() {

    val isLoading = MutableLiveData<Boolean>()

    var offerId = 0L
    var title = "Castle Acre Ruins"
    var videoId = ""
    var imageUrl = ""
    var description = "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
    var fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
    var category = ""
    var location: Location? = null

    fun initData(any: Serializable?) {
        when (any) {
            is LocationOfferDB -> {
                offerId = any.id
                title = any.title
                any.videoId.let {
                    videoId = if (it.isNotBlank()) {
                        it
                    } else {
                        ""
                    }
                }
                imageUrl = any.image
                description = any.description
                fullAddress = any.fullAddress
                category = any.category
            }
            is SpecialOfferDB -> {
                offerId = any.id
                title = any.title
                any.videoId.let {
                    videoId = if (it.isNotBlank()) {
                        it
                    } else {
                        ""
                    }
                }
                imageUrl = any.image
                description = any.description
                fullAddress = any.fullAddress
                category = CATEGORY_SPECIAL_OFFER
            }
            null -> {
                offerId = 0L
                title = "Castle Acre Ruins"
                videoId = ""
                description =
                    "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
                fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
                category = CATEGORY_EAT_AND_DRINK
            }
            else -> {
                offerId = 0L
                title = "Castle Acre Ruins"
                videoId = ""
                description =
                    "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
                fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
                category = CATEGORY_EAT_AND_DRINK
            }
        }
    }

    fun collectOffer() {
        isLoading.value = true
        launchCoroutine({
            val response = repository.collectOffer(
                offerId = offerId,
                lat = location?.latitude ?: 0.0,
                lng = location?.longitude ?: 0.0
            ).await()
            if (response.success == 1) {
                checkAndUpdateProfile(true)
            } else if (response.error == 1) {
                showError(response.errorMessage)
            }
            isLoading.value = false
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    private fun checkAndUpdateProfile(isRefresh: Boolean) {
        launchCoroutine({
            val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.getTableCacheData(TableNames.Profile)
            }
            val lastData: TableCache =
                if (data.isEmpty()) TableCache(TableNames.Profile, 0, 0.0, 0.0, true) else data[0]
            val needUpdate = System.currentTimeMillis() - lastData.lastUpdate > Constants.DATABASE_UPDATE_INTERVAL
            if (needUpdate || isRefresh) {
                isLoading.value = true && !isRefresh
                val response = repository.getProfile().await()
                if (response.errorMessage == null) {
                    lastData.needUpdate = true
                    withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                        repository.insertTableCacheData(lastData)
                        response.profile?.let {
                            repository.updateProfileDB(ProfileMapper().map(it))
                        }
                        response.wallets?.getOrNull(0)?.apply {
                            stars?.let {
                                lastData.tableName = TableNames.Star
                                repository.insertTableCacheData(lastData)
                                repository.updateStarsDB(StarsMapper().map(it))
                            }
                            offers?.let {
                                lastData.tableName = TableNames.Offer
                                repository.insertTableCacheData(lastData)
                                repository.updateOffersDB(OffersMapper().map(it))
                            }
                        }
                    }
                    if (isRefresh) {
                        onOfferCollected()
                    }
                    if (response.wallets?.getOrNull(0)?.offers?.isEmpty() != false) {
                        isLoading.value = false
                    }
                } else {
                    isLoading.value = false
                    showError(response.errorMessage)
                }
            }
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    fun onBack() {
        _eventsLiveData.value = Event(OfferDialogFragment.Events.BACK)
    }

    fun onOfferCollected() {
        _eventsLiveData.value = Event(OfferDialogFragment.Events.OFFER_COLLECTED)
    }

    fun closeDialog() {
        onBack()
    }

    companion object {
        const val CATEGORY_EAT_AND_DRINK = "eatAndDrink"
        const val CATEGORY_FREE_FUN = "freefun"
        const val CATEGORY_ATTRACTIONS = "attractions"
        const val CATEGORY_SPECIAL_OFFER = "specialOffer"
    }
}