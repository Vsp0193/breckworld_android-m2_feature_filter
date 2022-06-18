package com.breckworld.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.*
import com.breckworld.activity.PasscodeActivity
import com.breckworld.repository.Repository2
import com.breckworld.utils.BaseViewModel2
import com.breckworld.app.repository.ResponseListener
import com.breckworld.webservice.ConvertJsonToMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class SignupVM(private val repository: Repository2) : BaseViewModel2() {

    fun signupApiCall(lifeCycle: LifecycleOwner, activity: Activity, firstName: String, lastName: String, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request_data = JSONObject()
            request_data.put("first_name", firstName)
            request_data.put("last_name", lastName)
            request_data.put("email", email)
            repository.signupApiCall(ConvertJsonToMap().jsonToMap(request_data) as Map<String?, Any>)
        }

        repository.signupLiveData.observe(lifeCycle, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    repository.signupLiveData.removeObservers(lifeCycle)
                    if (it.data?.status != null) {
                        val intent = Intent(activity, PasscodeActivity::class.java)
                        intent.putExtra("email", email)
                        intent.putExtra("cameFrom", "Register")
                        activity.startActivity(intent)
                        activity.finish()
                    } else {
                        showShortMsg(activity, it.data?.error_description.toString())
                        //showShortMsg(activity, "Please enter different email address it's already taken.")
                    }
                }
                is ResponseListener.Failure -> {
                    repository.signupLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
                is ResponseListener.Error -> {
                    repository.signupLiveData.removeObservers(lifeCycle)
                    showShortMsg(activity, it.errorMessage.toString())
                }
            }
        })
    }

}