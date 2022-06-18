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
import com.breckworld.util.AppUtil
import com.breckworld.webservice.ConvertJsonToMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class FeedBackVM(private val repository: Repository2) : BaseViewModel2() {

    fun sendFeedBackApiCall(
        lifeCycle: LifecycleOwner,
        activity: Activity,
        access_token: String,
        message: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val request_data = JSONObject()
            request_data.put("message", message)
            repository.sendFeedBackApiCall(
                access_token,
                ConvertJsonToMap().jsonToMap(request_data) as Map<String?, Any>
            )
        }

        repository.feedBackLiveData.observe(lifeCycle, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    repository.feedBackLiveData.removeObservers(lifeCycle)
                    if (it.data?.status != null && !it.data?.status.equals("")) {
                        AppUtil.hideSoftKeyboard(activity)
                        showShortMsg(activity, it.data.message)
                        activity.finish()
                    } else {
                        showShortMsg(activity, it.data?.error_description.toString())
                    }
                }
                is ResponseListener.Failure -> {
                    repository.feedBackLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
                is ResponseListener.Error -> {
                    repository.feedBackLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
            }
        })
    }

}