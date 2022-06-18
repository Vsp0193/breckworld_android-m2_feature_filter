package com.breckworld.ui.main.settings

import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event

/**
 * @author Dmytro Bondarenko
 *         Date: 08.06.2019
 *         Time: 15:56
 *         E-mail: bondes87@gmail.com
 */
class SettingsViewModel : BaseMVVMViewModel<SettingsFragment.Events>() {

    fun onTermsOfService() {
        _eventsLiveData.value = Event(SettingsFragment.Events.TERMS_OF_SERVICE)
    }

    fun onPrivacyPolicy() {
        _eventsLiveData.value = Event(SettingsFragment.Events.PRIVACY_POLICY)
    }

    fun onStartTutorial() {
        _eventsLiveData.value = Event(SettingsFragment.Events.START_TUTORIAL)
    }

    fun onReportIssue() {
        _eventsLiveData.value = Event(SettingsFragment.Events.REPORT_ISSUE)
    }
}