package com.breckworld.ui.login.signIn

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.remote.http.model.LoginRequest
import com.breckworld.utils.Utils

/**
 * @author Dmytro Bondarenko
 *         Date: 25.05.2019
 *         Time: 20:10
 *         E-mail: bondes87@gmail.com
 */
class SignInViewModel : BaseMVVMViewModel<SignInFragment.Events>() {

    val isLoginBtnEnabled = MediatorLiveData<Boolean>()
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    init {
        val observer = Observer<String> {
            isLoginBtnEnabled.value = isCredentialValid()
        }
        isLoginBtnEnabled.addSource(username, observer)
        isLoginBtnEnabled.addSource(password, observer)
    }

    fun onBack() {
        _eventsLiveData.value = Event(SignInFragment.Events.BACK)
    }

    fun onLogIn() {
        showLoading()
        launchCoroutine({
            val response = repository.login(createLoginRequest()).await()
            hideLoading()
            if (response.errorDescription == null) {
                if (repository.isFirstRun()) {
                    repository.saveFirstRun(false)
                    _eventsLiveData.value = Event(SignInFragment.Events.SHOW_TUTORIAL)
                } else {
                    _eventsLiveData.value = Event(SignInFragment.Events.LOG_IN)
                    showError(App.applicationContext().getString(R.string.login_success))
                }
                repository.saveToken(response.tokenType + " " + response.accessToken)
                response.expiresIn?.let { repository.saveExpiresTime(Utils.getMillisecondFromGreenwich() / 1000 + it) }
            } else {
                showError(response.errorDescription)
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }

    private fun createLoginRequest() = LoginRequest(username = username.value, password = password.value)

    fun onCreateAccountOrLogin() {
        _eventsLiveData.value = Event(SignInFragment.Events.CREATE_ACCOUNT_OR_LOGIN)
    }

    fun onTermsOfService() {
        _eventsLiveData.value = Event(SignInFragment.Events.TERMS_OF_SERVICE)
    }

    fun onPrivacyPolicy() {
        _eventsLiveData.value = Event(SignInFragment.Events.PRIVACY_POLICY)
    }

    private fun isCredentialValid(): Boolean {
        return !username.value.isNullOrBlank() &&
                !password.value.isNullOrBlank() &&
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