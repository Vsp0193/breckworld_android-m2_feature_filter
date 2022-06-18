package com.breckworld.ui.main.home

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event

class HomeViewModel : BaseMVVMViewModel<HomeFragment.Events>() {
    fun onBack() {
        _eventsLiveData.value = Event(HomeFragment.Events.BACK)
    }

    fun isGuideHome(): Boolean {
        return repository.isGuideHome()
    }

    fun saveGuideHome(isGuideShow: Boolean) {
        repository.saveGuideHome(isGuideShow)
    }
}