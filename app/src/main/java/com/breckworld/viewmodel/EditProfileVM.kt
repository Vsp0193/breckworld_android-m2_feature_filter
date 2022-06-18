package com.breckworld.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.*
import com.breckworld.App
import com.breckworld.activity.HomeActivity
import com.breckworld.activity.PasscodeActivity
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.model.userprofile.Profile
import com.breckworld.webservice.ConvertJsonToMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class EditProfileVM(private val repository: Repository2) : BaseViewModel2() {

    fun editProfileApiCall(
        lifeCycle: LifecycleOwner,
        activity: Activity,
        access_token: String,
        firstName: String,
        lastName: String,
        picture: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val request_data = JSONObject()
            request_data.put("first_name", firstName)
            request_data.put("last_name", lastName)
            request_data.put("picture", picture)
            repository.editProfileApiCall(
                access_token,
                ConvertJsonToMap().jsonToMap(request_data) as Map<String?, Any>
            )
        }

        repository.editProfileLiveData.observe(lifeCycle, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    repository.editProfileLiveData.removeObservers(lifeCycle)
                    if (it.data?.success != null) {
                        showShortMsg(activity, "Profile updated successfully.")
                        /*val profile: Profile = App.mLocalStore?.getUserProfile()!!
                        profile.first_name = firstName
                        profile.last_name = lastName
                        profile.profile_pic = picture
                        App.mLocalStore?.setUserProfile(profile)*/
                        activity.startActivity(Intent(activity, HomeActivity::class.java))
                        activity.finishAffinity()
                    } else {
                        showShortMsg(activity, it.data?.error_description.toString())
                    }
                }
                is ResponseListener.Failure -> {
                    repository.editProfileLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
                is ResponseListener.Error -> {
                    repository.editProfileLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
            }
        })
    }

}