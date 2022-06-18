package com.breckworld.ui.main.editProfile

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.mapper.OffersMapper
import com.breckworld.repository.database.mapper.ProfileMapper
import com.breckworld.repository.database.mapper.StarsMapper
import com.breckworld.repository.database.model.ProfileDB
import com.breckworld.repository.database.model.TableCache
import com.breckworld.repository.database.model.TableNames
import com.breckworld.repository.remote.http.model.UpdateProfileRequest
import com.breckworld.utils.Utils
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @author Dmytro Bondarenko
 *         Date: 10.06.2019
 *         Time: 14:44
 *         E-mail: bondes87@gmail.com
 */
class EditProfileViewModel : BaseMVVMViewModel<EditProfileFragment.Events>() {

    val profile = repository.getProfileDBLiveData()

    val isSaveBtnEnabled = MediatorLiveData<Boolean>()
    val profileAvatar = MutableLiveData<String>()
    val profileAvatarFile = MutableLiveData<File>()
    val firstName = MutableLiveData<String>()
    private var firstNameOld = ""
    val lastName = MutableLiveData<String>()
    private var lastNameOld = ""
    var isAvatarChanged = MutableLiveData<Boolean>()


    init {
        val observer = Observer<Any> {
            isSaveBtnEnabled.value = isDataChanged()
        }
        isSaveBtnEnabled.addSource(firstName, observer)
        isSaveBtnEnabled.addSource(lastName, observer)
        isSaveBtnEnabled.addSource(profileAvatarFile, observer)
        isSaveBtnEnabled.addSource(isAvatarChanged, observer)
    }

    fun initData(profileDB: ProfileDB) {
        isAvatarChanged.value = false
        if (profileAvatarFile.value == null) {
            profileAvatar.value = profileDB.profilePic
            firstName.value = profileDB.firstName
            lastName.value = profileDB.lastName
        }
        firstNameOld = profileDB.firstName
        lastNameOld = profileDB.lastName
    }

    private fun isDataChanged(): Boolean {
        return (profileAvatarFile.value != null &&
                isAvatarChanged.value != null &&
                isAvatarChanged.value!!) ||
                (lastName.value?.trim() != lastNameOld.trim() ||
                firstName.value?.trim() != firstNameOld.trim()) &&
                !firstName.value.isNullOrBlank() &&
                !lastName.value.isNullOrBlank()

    }

    fun onImageClick() {
        _eventsLiveData.value = Event(EditProfileFragment.Events.PICK_IMAGE)
    }

    fun onSave() {
        showLoading()
        launchCoroutine({
            val response = repository.updateProfile(
                UpdateProfileRequest(
                    firstName.value, lastName.value, picture = if (profileAvatarFile.value != null) {
                        encodeToBase64()
                    } else {
                        null
                    }
                )
            ).await()
            if (response.errorMessage == null) {
                firstNameOld = firstName.value ?: firstNameOld
                lastNameOld = lastName.value ?: lastNameOld
                updateProfileBD()
            } else {
                hideLoading()
                showError(response.errorMessage)
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }

    private fun encodeToBase64(): String? {
        return Utils.getBytesFromFile(profileAvatarFile.value!!)?.let { Utils.encodeToBase64(it) } ?: ""
    }

    private fun updateProfileBD() {
        launchCoroutine({
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
            hideLoading()
        },
            { _, throwable ->
                hideLoading()
                showError(throwable.message)
            })
    }
}