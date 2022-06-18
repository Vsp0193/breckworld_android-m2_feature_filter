package com.breckworld.ui.login.signUp

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentSignUpBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_terms_of_service.*

/**
 * @author Dmytro Bondarenko
 *         Date: 27.05.2019
 *         Time: 16:10
 *         E-mail: bondes87@gmail.com
 */
class SignUpFragment : BaseMVVMFragment<SignUpViewModel, FragmentSignUpBinding, SignUpFragment.Events>() {

    override fun viewModelClass(): Class<SignUpViewModel> = SignUpViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_sign_up

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.BACK -> findNavController().navigateUp()
                Events.SIGN_IN -> {
                    val bundle = bundleOf(
                        Constants.KEY_USERNAME to viewModel.username.value,
                        Constants.KEY_PASSWORD to viewModel.password.value
                    )
                    findNavController().navigateTo(R.id.action_signUpFragment_to_signInFragment2, bundle)
                }
                Events.TERMS_OF_SERVICE -> findNavController().navigateTo(R.id.action_global_termsOfServiceFragment)
                Events.PRIVACY_POLICY -> findNavController().navigateTo(R.id.action_global_privacyPolicyFragment)
                Events.CREATE_ACCOUNT_OR_LOGIN -> findNavController().navigateUp()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupStatusbar(true, true)
        setupStatusBarPadding()
    }

    private fun setupStatusBarPadding() {
        val top = scroll_view_content.paddingTop + getStatusBarHeight()
        val bottom = scroll_view_content.paddingBottom
        val start = scroll_view_content.paddingStart
        val end = scroll_view_content.paddingEnd
        scroll_view_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        BACK,
        SIGN_IN,
        CREATE_ACCOUNT_OR_LOGIN,
        TERMS_OF_SERVICE,
        PRIVACY_POLICY
    }
}