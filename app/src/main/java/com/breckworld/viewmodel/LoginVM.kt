package com.breckworld.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.*
import com.breckworld.App
import com.breckworld.R
import com.breckworld.activity.IntroActivity
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.model.forgotpasscode.ForgotPasscodeModel
import com.breckworld.model.login.LoginModel
import com.breckworld.model.mds.MdsModel
import com.breckworld.webservice.ConvertJsonToMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginVM(private val repository: Repository2) : BaseViewModel2() {

    val loginLiveData: LiveData<ResponseListener<LoginModel>>
        get() = repository.loginLiveData

    fun loginApiCall(lifeCycle: LifecycleOwner, activity: Activity, email: String, pass: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request_data = JSONObject()
            request_data.put("client_id", "ViewingAPI1.0/iOSApp")
            request_data.put("client_secret", "TESTINGSECRETKEY")
            request_data.put("grant_type", "password")
            request_data.put("scope", "")
            request_data.put("email", email)
            request_data.put("passcode", pass)
            repository.loginApiCall(ConvertJsonToMap().jsonToMap(request_data) as Map<String?, Any>)
        }
    }

    fun forgotPasscodeApiCall(lifeCycle: LifecycleOwner, activity: Activity, cameFrom: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request_data = JSONObject()
            request_data.put("email", email)
            repository.forgotPasscodeApiCall(ConvertJsonToMap().jsonToMap(request_data) as Map<String?, Any>)
        }

        repository.forgotPasscodeLiveData.observe(lifeCycle, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    repository.forgotPasscodeLiveData.removeObservers(lifeCycle)
                    if (it.data?.status != null) {
                        /*if (cameFrom.equals("Login")) {
                            showShortMsg(activity, it.data.message)
                        } else {
                        }*/
                        showShortMsg(activity, activity.resources.getString(R.string.we_have_sent_a_new_passcode_to_your_registered_email_id))
                    } else {
                        showShortMsg(activity, it.data?.error_description.toString())
                    }
                }
                is ResponseListener.Failure -> {
                    repository.forgotPasscodeLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
                is ResponseListener.Error -> {
                    repository.forgotPasscodeLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
            }
        })
    }

}