package com.breckworld.ui.login.privacyPolicy

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentPrivacyPolicyBinding
import com.breckworld.livedata.EventObserver
import com.breckworld.util.AppUtil
import com.breckworld.utils.IntentHelper
import kotlinx.android.synthetic.main.fragment_privacy_policy.*

/**
 * @author Dmytro Bondarenko
 *         Date: 28.05.2019
 *         Time: 17:24
 *         E-mail: bondes87@gmail.com
 */
class PrivacyPolicyFragment :
    BaseMVVMFragment<PrivacyPolicyViewModel, FragmentPrivacyPolicyBinding, PrivacyPolicyFragment.Events>() {

    override fun viewModelClass(): Class<PrivacyPolicyViewModel> = PrivacyPolicyViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_privacy_policy

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //AppUtil.makeStatusBarTransparent(1, requireActivity())
        //setupStatusbar(true, true)
        setupStatusBarPadding()
        initViews()
        binding.imageViewBack.setOnClickListener(View.OnClickListener {
            AppUtil.hideSoftKeyboard(requireContext())
            activity?.finish()
        })
    }

    private fun initViews() {
        val ss = SpannableString(getString(R.string.privacy_policy_description))
        val url = getString(R.string.privacy_url)
        val email = getString(R.string.privacy_email)
        val clickableUrlSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                IntentHelper.openInBrowser(context!!, url)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(clickableUrlSpan, ss.indexOf(url), ss.indexOf(url) + url.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val clickableMailSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                IntentHelper.sendToMail(context!!, email)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
            }
        }
        ss.setSpan(
            clickableMailSpan, ss.indexOf(email), ss.indexOf(email) + email.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text_view_description.text = ss
        text_view_description.movementMethod = LinkMovementMethod.getInstance()
        text_view_description.highlightColor = Color.TRANSPARENT
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