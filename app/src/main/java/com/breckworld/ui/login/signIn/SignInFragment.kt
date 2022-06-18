package com.breckworld.ui.login.signIn

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentSignInBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.findNavController
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_sign_in.*

/**
 * @author Dmytro Bondarenko
 *         Date: 25.05.2019
 *         Time: 20:10
 *         E-mail: bondes87@gmail.com
 */
class SignInFragment : BaseMVVMFragment<SignInViewModel, FragmentSignInBinding, SignInFragment.Events>() {

    override fun viewModelClass(): Class<SignInViewModel> = SignInViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_sign_in

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.BACK -> findNavController().navigateUp()
                Events.LOG_IN -> {
                    findNavController().navigateTo(R.id.action_signInFragment_to_mainActivity)
                    activity?.finish()
                }
                Events.TERMS_OF_SERVICE -> findNavController(R.id.container)?.navigateTo(R.id.action_global_termsOfServiceFragment)
                Events.PRIVACY_POLICY -> findNavController().navigateTo(R.id.action_global_privacyPolicyFragment)
                Events.CREATE_ACCOUNT_OR_LOGIN -> findNavController().navigateUp()
                Events.SHOW_TUTORIAL -> findNavController().navigateTo(R.id.action_signInFragment_to_tutorialFragment2,
                    bundleOf(Constants.KEY_FIRST_RUN to true))
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            viewModel.username.value = getString(Constants.KEY_USERNAME)
            viewModel.password.value = getString(Constants.KEY_PASSWORD)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusbar(true, true)
        setupStatusBarPadding()
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        BACK,
        LOG_IN,
        CREATE_ACCOUNT_OR_LOGIN,
        TERMS_OF_SERVICE,
        PRIVACY_POLICY,
        SHOW_TUTORIAL
    }
}