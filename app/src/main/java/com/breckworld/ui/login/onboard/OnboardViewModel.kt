package com.breckworld.ui.login.onboard

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event

/**
 * @author Dmytro Bondarenko
 *         Date: 21.05.2019
 *         Time: 20:57
 *         E-mail: bondes87@gmail.com
 */
class OnboardViewModel : BaseMVVMViewModel<OnboardFragment.Events>() {

    fun onSignUp() {
        _eventsLiveData.value = Event(OnboardFragment.Events.SIGN_UP)
    }

    fun onLogIn() {
        _eventsLiveData.value = Event(OnboardFragment.Events.LOG_IN)
    }

    fun onTermsOfService() {
        _eventsLiveData.value = Event(OnboardFragment.Events.TERMS_OF_SERVICE)
    }

    fun onPrivacyPolicy() {
        _eventsLiveData.value = Event(OnboardFragment.Events.PRIVACY_POLICY)
    }
}