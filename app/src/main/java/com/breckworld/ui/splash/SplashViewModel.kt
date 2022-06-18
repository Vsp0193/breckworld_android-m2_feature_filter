package com.breckworld.ui.splash

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.utils.Utils

class SplashViewModel : BaseMVVMViewModel<SplashActivity.Events>() {

    fun moveNext() {
        if (repository.getToken().isEmpty() ||
            repository.getExpiresTime() <= Utils.getMillisecondFromGreenwich()/1000
        ) {
            repository.logout()
            openLoginActivity()
        } else {
            openMainActivity()
        }
    }

    fun openLoginActivity() {
        _eventsLiveData.value = Event(SplashActivity.Events.OPEN_LOGIN)
    }

    fun openMainActivity() {
        _eventsLiveData.value = Event(SplashActivity.Events.OPEN_MAIN)
    }

}