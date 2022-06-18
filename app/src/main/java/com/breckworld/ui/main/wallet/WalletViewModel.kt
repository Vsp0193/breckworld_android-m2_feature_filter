package com.breckworld.ui.main.wallet

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
 *         Date: 07.06.2019
 *         Time: 12:20
 *         E-mail: bondes87@gmail.com
 */
class WalletViewModel : BaseMVVMViewModel<WalletFragment.Events>() {

    val offers = repository.getOffersDBLiveData()
    val profile = repository.getProfileDBLiveData()
    val profileAvatar = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()

    fun checkAndUpdateProfile(isRefresh: Boolean) {
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
                        _eventsLiveData.value = Event(WalletFragment.Events.REFRESH_CLOSE)
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

    fun onClickSettings() {
        _eventsLiveData.value = Event(WalletFragment.Events.SETTINGS)
    }

    fun onClickProfile() {
        _eventsLiveData.value = Event(WalletFragment.Events.PROFILE)
    }

    fun isGuideWallet(): Boolean {
        return repository.isGuideWallet()
    }

    fun saveGuideWallet(isGuideShow: Boolean) {
        repository.saveGuideWallet(isGuideShow)
    }
}