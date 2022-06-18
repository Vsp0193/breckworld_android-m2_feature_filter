package com.breckworld.ui.login.onboard

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentOnboardBinding
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_onboard.*

/**
 * @author Dmytro Bondarenko
 *         Date: 21.05.2019
 *         Time: 20:57
 *         E-mail: bondes87@gmail.com
 */
class OnboardFragment : BaseMVVMFragment<OnboardViewModel, FragmentOnboardBinding, OnboardFragment.Events>() {

    override fun viewModelClass(): Class<OnboardViewModel> = OnboardViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_onboard

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.SIGN_UP -> findNavController().navigateTo(R.id.action_onboard_fragment_to_signUpFragment)
                Events.LOG_IN -> findNavController().navigateTo(R.id.action_onboard_fragment_to_signInFragment)
                Events.TERMS_OF_SERVICE -> findNavController().navigateTo(R.id.action_global_termsOfServiceFragment)
                Events.PRIVACY_POLICY -> findNavController().navigateTo(R.id.action_global_privacyPolicyFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusbar(true, true)
        setupStatusBarPadding()
    }

    private fun setupStatusBarPadding() {
        //TODO: fix getStatusBarHeigh problem
        //val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val top = constraint_layout_content.paddingTop + 20;
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        SIGN_UP,
        LOG_IN,
        TERMS_OF_SERVICE,
        PRIVACY_POLICY
    }
}