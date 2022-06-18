package com.breckworld.ui.main.reportIssue

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentReportIssueBinding
import com.breckworld.util.AppUtil
import kotlinx.android.synthetic.main.fragment_report_issue.*


/**
 * @author Dmytro Bondarenko
 *         Date: 14.06.2019
 *         Time: 16:47
 *         E-mail: bondes87@gmail.com
 */
class ReportIssueFragment :
    BaseMVVMFragment<ReportIssueViewModel, FragmentReportIssueBinding, ReportIssueFragment.Events>() {

    override fun viewModelClass(): Class<ReportIssueViewModel> = ReportIssueViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_report_issue

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //AppUtil.makeStatusBarTransparent(1, requireActivity())
        showBottomNavigation(false)
        //setupStatusbar(true, true)
        setupStatusBarPadding()

        binding.imageViewBack.setOnClickListener(View.OnClickListener {
            AppUtil.hideSoftKeyboard(requireContext())
            activity?.finish()
        })
    }

    override fun onStart() {
        super.onStart()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }

    override fun onStop() {
        super.onStop()
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    private fun setupStatusBarPadding() {
        val top = scroll_view_content.paddingTop + getStatusBarHeight()
        val bottom = scroll_view_content.paddingBottom
        val start = scroll_view_content.paddingStart
        val end = scroll_view_content.paddingEnd
        scroll_view_content.setPadding(start, top, end, bottom)
    }

    enum class Events
}