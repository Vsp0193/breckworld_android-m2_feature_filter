package com.breckworld.ui.main.starsTab.collectedStars

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.extensions.Constants
import com.breckworld.livedata.Event
import com.breckworld.repository.database.mapper.OffersMapper
import com.breckworld.repository.database.mapper.ProfileMapper
import com.breckworld.repository.database.mapper.StarsMapper
import com.breckworld.repository.database.model.TableCache
import com.breckworld.repository.database.model.TableNames
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 22:15
 *         E-mail: bondes87@gmail.com
 */
class CollectedStarsViewModel : BaseMVVMViewModel<CollectedStarsFragment.Events>() {

    val collectedStars = repository.getStarsDBLiveData()
    val isLoading = MutableLiveData<Boolean>()

    fun checkAndUpdateStars(isRefresh: Boolean) {
        launchCoroutine({
            val data = withContext(repository.databaseExecutor.asCoroutineDispatcher()) {
                repository.getTableCacheData(TableNames.Star)
            }
            val lastData: TableCache =
                if (data.isEmpty()) TableCache(TableNames.Star, 0, 0.0, 0.0, true) else data[0]
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
                        _eventsLiveData.value = Event(CollectedStarsFragment.Events.REFRESH_CLOSE)
                    }
                    if (response.wallets?.getOrNull(0)?.stars?.isEmpty() != false) {
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
}