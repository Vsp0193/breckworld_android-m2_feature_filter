package com.breckworld.ui.main.tutorial

import androidx.lifecycle.MutableLiveData
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event

class TutorialViewModel : BaseMVVMViewModel<TutorialFragment.Events>() {

    var counter = MutableLiveData<Int>()
    var isFirstRun = false

    init {
        counter.value = 1
    }

    fun onNext() {
        if (counter.value!! < MAX_COUNTER) {
            counter.value = counter.value!! + 1
        } else {
            if (isFirstRun) {
                _eventsLiveData.value = Event(TutorialFragment.Events.OPEN_MAIN_SCREEN)
            } else {
                repository.startGuide()
                _eventsLiveData.value = Event(TutorialFragment.Events.START_TUTORIAL)
            }
        }
    }

    companion object {
        const val MAX_COUNTER = 3
    }
}