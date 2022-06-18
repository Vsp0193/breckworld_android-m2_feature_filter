package com.breckworld.ui.main.profile

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.ProfileDB

/**
 * @author Dmytro Bondarenko
 *         Date: 10.06.2019
 *         Time: 11:41
 *         E-mail: bondes87@gmail.com
 */
class ProfileViewModel : BaseMVVMViewModel<ProfileFragment.Events>() {

    val profile = repository.getProfileDBLiveData()
    val profileAvatar = MutableLiveData<String>()
    val fullName = MutableLiveData<String>()

    fun initData(profileDB: ProfileDB) {
        profileAvatar.value = profileDB.profilePic
        val firsName = profileDB.firstName
        val lastName = profileDB.lastName
        fullName.value = when {
            firsName.isNotBlank() && lastName.isNotBlank() -> "$firsName $lastName"
            firsName.isNotBlank() -> firsName
            lastName.isNotBlank() -> lastName
            else -> profileDB.userLogin
        }
    }

    fun onEditProfile() {
        _eventsLiveData.value = Event(ProfileFragment.Events.EDIT_PROFILE)
    }

    fun onSettings() {
        _eventsLiveData.value = Event(ProfileFragment.Events.SETTINGS)
    }

    fun onLogOut() {
        repository.logout()
        _eventsLiveData.value = Event(ProfileFragment.Events.LOG_OUT)
    }
}