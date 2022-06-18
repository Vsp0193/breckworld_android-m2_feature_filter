package com.breckworld.ui.login.termsOfService

import android.os.Bundle
import android.view.View
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentTermsOfServiceBinding
import com.breckworld.livedata.EventObserver
import com.breckworld.util.AppUtil
import kotlinx.android.synthetic.main.fragment_terms_of_service.*

/**
 * @author Dmytro Bondarenko
 *         Date: 28.05.2019
 *         Time: 14:42
 *         E-mail: bondes87@gmail.com
 */
class TermsOfServiceFragment :
    BaseMVVMFragment<TermsOfServiceViewModel, FragmentTermsOfServiceBinding, TermsOfServiceFragment.Events>() {

    override fun viewModelClass(): Class<TermsOfServiceViewModel> = TermsOfServiceViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_terms_of_service

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //AppUtil.makeStatusBarTransparent(1, requireActivity())
        //setupStatusbar(false, true)
        setupStatusBarPadding()
        binding.imageViewBack.setOnClickListener(View.OnClickListener {
            AppUtil.hideSoftKeyboard(requireContext())
            activity?.finish()
        })
    }

    private fun setupStatusBarPadding() {
        val top = scroll_view_content.paddingTop + getStatusBarHeight()
        val bottom = scroll_view_content.paddingBottom
        val start = scroll_view_content.paddingStart
        val end = scroll_view_content.paddingEnd
        scroll_view_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
    }
}