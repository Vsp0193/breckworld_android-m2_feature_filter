package com.breckworld.ui.main.starsTab.starsClues

import android.location.Location
import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.repository.database.mapper.LocationStarsMapper
import com.breckworld.repository.database.model.TableCache
import com.breckworld.repository.database.model.TableNames
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import uk.co.appoly.arcorelocation.utils.LocationUtils

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 13:19
 *         E-mail: bondes87@gmail.com
 */
class StarsCluesViewModel : BaseMVVMViewModel<StarsCluesFragment.Events>() {

    val starsClues = repository.getLocationStarsDBLiveData()
    val isLoading = MutableLiveData<Boolean>()
    var currentLocation: Location? = null

    fun checkAndUpdateStars(isRefresh: Boolean) {
        launchCoroutine({
            val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.getTableCacheData(TableNames.LocationStar)
            }
            val lastData: TableCache = if (data.isEmpty()) {
                TableCache(TableNames.LocationStar, 0, 0.0, 0.0, true)
            } else {
                data[0]
            }
            if (isNeedUpdate(lastData, currentLocation!!) || isRefresh) {
                currentLocation?.let { reloadData(lastData, isRefresh) }
            }
        }, { _, throwable ->
            isLoading.value = false
            showError(throwable.message)
        })
    }

    private suspend fun reloadData(lastData: TableCache, isRefresh: Boolean) {
        isLoading.value = true && !isRefresh
        val response = repository.getAllObjects(
            currentLocation?.latitude,
            currentLocation?.longitude,
            RADIUS_IN_METERS,
            FILTER_TYPE_ALL
        ).await()
        if (response.errorMessage == null) {
            lastData.needUpdate = true
            withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.insertTableCacheData(lastData)
                response.locationStars?.let {
                    repository.updateLocationStarsDB(
                        LocationStarsMapper().map(it),
                        currentLocation!!
                    )
                }
            }
            if (isRefresh) {
                _eventsLiveData.value = Event(StarsCluesFragment.Events.REFRESH_CLOSE)
            }
            if (response.locationStars?.isEmpty() != false) {
                isLoading.value = false
            }
        } else {
            isLoading.value = false
            showError(response.errorMessage)
        }
    }

    private fun isNeedUpdate(
        lastData: TableCache,
        location: Location
    ): Boolean {
        return System.currentTimeMillis() - lastData.lastUpdate > Constants.DATABASE_UPDATE_INTERVAL ||
                LocationUtils.distance(
                    lastData.lat!!, location.latitude,
                    lastData.lng!!, location.longitude,
                    0.0, 0.0
                ) > DELTA_DISTANCE_IN_METERS
    }

    companion object {
        const val FILTER_TYPE_ALL = "all"
        const val RADIUS_IN_METERS: Int = 3000
        const val DELTA_DISTANCE_IN_METERS: Int = 500
    }
}