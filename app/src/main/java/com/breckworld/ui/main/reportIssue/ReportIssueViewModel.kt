package com.breckworld.ui.main.reportIssue

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.repository.remote.http.model.ReportIssueRequest


/**
 * @author Dmytro Bondarenko
 *         Date: 14.06.2019
 *         Time: 17:01
 *         E-mail: bondes87@gmail.com
 */

class ReportIssueViewModel : BaseMVVMViewModel<ReportIssueFragment.Events>() {

    val isSendBtnEnabled = MediatorLiveData<Boolean>()
    val message = MutableLiveData<String>()

    init {
        val observer = Observer<Any> {
            isSendBtnEnabled.value = !message.value.isNullOrBlank()

        }
        isSendBtnEnabled.addSource(message, observer)
    }

    fun onSend() {
        showLoading()
        launchCoroutine({
            val response = repository.sendFeedback(ReportIssueRequest(message.value)).await()
            hideLoading()
            if (response.success == 1) {
                message.value = ""
                showError(App.getStringFromRes(R.string.report_issue_success_message))
            } else {
                showError(App.getStringFromRes(R.string.error))
            }
        }, { _, throwable ->
            hideLoading()
            showError(throwable.message)
        })
    }
}