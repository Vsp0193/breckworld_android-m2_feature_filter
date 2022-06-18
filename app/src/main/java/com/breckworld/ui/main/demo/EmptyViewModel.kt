package com.breckworld.ui.main.demo

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event

class EmptyViewModel : BaseMVVMViewModel<EmptyFragment.Events>() {
    fun onBack() {
        _eventsLiveData.value = Event(EmptyFragment.Events.BACK)
    }

    fun onLogout() {
        repository.logout()
        _eventsLiveData.value = Event(EmptyFragment.Events.EXIT)
    }
}