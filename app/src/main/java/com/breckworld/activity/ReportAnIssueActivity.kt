package com.breckworld.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.breckworld.App
import com.breckworld.R
import com.breckworld.databinding.ActivityEditProfileBinding
import com.breckworld.databinding.ActivityReportAnIssueBinding
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.EditProfileVM
import com.breckworld.viewmodel.FeedBackVM
import com.breckworld.viewmodel.factory.EditProfileVMFactory
import com.breckworld.viewmodel.factory.FeedBackVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface

class ReportAnIssueActivity : AppCompatActivity() {

    lateinit var reportAnIssueBinding: ActivityReportAnIssueBinding
    private var apiService: ApiInterface? = null
    private lateinit var feedBackVM: FeedBackVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        reportAnIssueBinding = ActivityReportAnIssueBinding.inflate(layoutInflater)
        setContentView(reportAnIssueBinding.root)
        apiService = ApiClient.createService(ApiInterface::class.java, this)
        val repositories = Repository2(apiService!!, this)

        feedBackVM = ViewModelProvider(
            this, FeedBackVMFactory(repositories)
        ).get(FeedBackVM::class.java)

        reportAnIssueBinding.imageViewBack.setOnClickListener {
            AppUtil.hideSoftKeyboard(this@ReportAnIssueActivity)
            super.onBackPressed()
        }

        reportAnIssueBinding.textViewSend.setOnClickListener {
            if (reportAnIssueBinding.editTextMessage.text.toString().trim().length <= 0) {
                Toast.makeText(
                    this@ReportAnIssueActivity,
                    resources.getString(R.string.please_enter_your_message),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                feedBackVM.sendFeedBackApiCall(
                    this@ReportAnIssueActivity,
                    this@ReportAnIssueActivity,
                    App.mLocalStore?.accessToken.toString(),
                    reportAnIssueBinding.editTextMessage.text.toString().trim()
                )
            }
        }
    }
}