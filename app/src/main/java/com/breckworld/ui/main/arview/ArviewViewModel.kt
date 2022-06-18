package com.breckworld.ui.main.arview

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.location.CurrentLocationLiveData
import com.breckworld.repository.database.mapper.*
import com.breckworld.repository.database.model.*
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import uk.co.appoly.arcorelocation.utils.LocationUtils

private const val STAR = "star"
private const val OFFER = "offer"
private const val FILTER_TYPE_ALL = "all"
private const val RADIUS_IN_METERS: Int = 3000
private const val DELTA_DISTANCE_IN_METERS: Int = 500

class ArviewViewModel : BaseMVVMViewModel<ArviewFragment.Events>() {
    val currentLocation = CurrentLocationLiveData.getInstance()

    val isLoading = MutableLiveData<Boolean>()
    val collectedStar = MutableLiveData<LocationStarDB>()

    val offers = repository.getLocationOffersDBLiveData()

    val presentStars = repository.getLocationStarsWithoutCollectedLiveData()

    fun checkAndUpdateStars(isRefresh: Boolean) {
        val location = currentLocation.value ?: return

        launchCoroutine({
            val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.getTableCacheData(TableNames.LocationStar)
            }
            val lastData: TableCache = if (data.isEmpty()) {
                TableCache(TableNames.LocationStar, 0, 0.0, 0.0, true)
            } else {
                data[0]
            }
            if (isNeedUpdate(lastData, location) || isRefresh) {
                reloadData(lastData, isRefresh, location)
            }
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    private suspend fun reloadData(lastData: TableCache, isRefresh: Boolean, location: Location) {
        isLoading.value = true && !isRefresh
        checkAndUpdateCollectedStars()
        val response = repository.getAllObjects(
            location.latitude,
            location.longitude,
            RADIUS_IN_METERS,
            FILTER_TYPE_ALL
        ).await()
        if (response.errorMessage == null) {
            lastData.needUpdate = true
            withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.insertTableCacheData(lastData)
                response.locationStars?.let {
                    repository.updateLocationStarsDB(LocationStarsMapper().map(it), location)
                }
                response.locationObjects?.let {
                    repository.updateLocationOffersDB(LocationOffersMapper().map(it), location)
                }
            }
            if (response.locationStars?.isEmpty() != false) {
                isLoading.value = false
            }
        } else {
            isLoading.value = false
            showError(response.errorMessage)
        }
    }

    fun collectStar(star: LocationStarDB) {
        isLoading.value = true
        launchCoroutine({
            val response = repository.collectStar(
                star.id,
                currentLocation.value?.latitude ?: 0.0,
                currentLocation.value?.longitude ?: 0.0,
                null,
                STAR
            ).await()
            if (response.success == 1) {
                checkAndUpdateCollectedStars()
            } else {
                showError(response.errorMessage)
            }
            isLoading.value = false
            collectedStar.value = star
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    private suspend fun checkAndUpdateCollectedStars() {
        val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
            repository.getTableCacheData(TableNames.Profile)
        }
        val lastData: TableCache =
            if (data.isEmpty()) TableCache(TableNames.Profile, 0, 0.0, 0.0, true) else data[0]

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
        } else {
            showError(response.errorMessage)
        }
    }

    private fun isNeedUpdate(
        lastData: TableCache,
        location: Location
    ): Boolean {
        return System.currentTimeMillis() - lastData.lastUpdate > Constants.DATABASE_UPDATE_INTERVAL ||
                LocationUtils.distance(
                    lastData.lat ?: 0.0, location.latitude,
                    lastData.lng ?: 0.0, location.longitude,
                    0.0, 0.0
                ) > DELTA_DISTANCE_IN_METERS
    }

    fun onBack() {
        _eventsLiveData.value = Event(ArviewFragment.Events.BACK)
    }

    fun isGuideArView(): Boolean {
        return repository.isGuideArView()
    }

    fun saveGuideArView(isGuideShow: Boolean) {
        repository.saveGuideArView(isGuideShow)
    }
}