package com.breckworld.ui.login.signUp

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.remote.http.model.SignUpRequest
import com.breckworld.utils.Utils

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 16:10
 *         E-mail: bondes87@gmail.com
 */
class SignUpViewModel : BaseMVVMViewModel<SignUpFragment.Events>() {

    val isSignUpBtnEnabled = MediatorLiveData<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()

    init {
        val observer = Observer<String> {
            isSignUpBtnEnabled.value = isCredentialValid()
        }
        isSignUpBtnEnabled.addSource(username, observer)
        isSignUpBtnEnabled.addSource(password, observer)
        isSignUpBtnEnabled.addSource(email, observer)
    }

    fun onBack() {
        _eventsLiveData.value = Event(SignUpFragment.Events.BACK)
    }

    fun onSignUp() {
        showLoading()
        launchCoroutine({
            val response = repository.signUp(createSignUpRequest()).await()
            hideLoading()
            if (response.errorMessage == null) {
                showError(App.applicationContext().getString(R.string.register_success))
                _eventsLiveData.value = Event(SignUpFragment.Events.SIGN_IN)
            } else {
                showError(response.errorMessage)
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }

    private fun createSignUpRequest() = SignUpRequest(
        username = username.value,
        password = password.value,
        firstName = firstName.value,
        lastName = lastName.value,
        email = email.value
    )


    fun onCreateAccountOrLogin() {
        _eventsLiveData.value = Event(SignUpFragment.Events.CREATE_ACCOUNT_OR_LOGIN)
    }

    fun onTermsOfService() {
        _eventsLiveData.value = Event(SignUpFragment.Events.TERMS_OF_SERVICE)
    }

    fun onPrivacyPolicy() {
        _eventsLiveData.value = Event(SignUpFragment.Events.PRIVACY_POLICY)
    }

    private fun isCredentialValid(): Boolean {
        return !username.value.isNullOrBlank() &&
                !password.value.isNullOrBlank() &&
                !email.value.isNullOrBlank() &&
                Utils.isEmailValid(email.value?.trim()) &&
                username.value?.length!! >= MIN_LENGTH_USERNAME &&
                password.value?.length!! >= MIN_LENGTH_PASSWORD &&
                username.value?.length!! <= MAX_LENGTH_USERNAME &&
                password.value?.length!! <= MAX_LENGTH_PASSWORD
    }

    companion object {
        const val MIN_LENGTH_PASSWORD: Int = 1
        const val MAX_LENGTH_PASSWORD: Int = 20
        const val MIN_LENGTH_USERNAME: Int = 1
        const val MAX_LENGTH_USERNAME: Int = 20
    }
}