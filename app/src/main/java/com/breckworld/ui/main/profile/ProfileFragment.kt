package com.breckworld.ui.main.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentProfileBinding
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_profile.*
import permissions.dispatcher.NeedsPermission

/**
 * @author Dmytro Bondarenko
 *         Date: 10.06.2019
 *         Time: 11:41
 *         E-mail: bondes87@gmail.com
 */
class ProfileFragment :
    BaseMVVMFragment<ProfileViewModel, FragmentProfileBinding, ProfileFragment.Events>() {

    override fun viewModelClass(): Class<ProfileViewModel> = ProfileViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_profile

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.LOG_OUT -> viewModel.logout()
                Events.EDIT_PROFILE -> findNavController().navigateTo(R.id.action_profileFragment_to_editProfileFragment)
                Events.SETTINGS -> findNavController().navigateTo(R.id.action_profileFragment_to_settingsFragment2)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, true)
        setupStatusBarPadding()
        viewModel.profile.removeObservers(this)
        viewModel.profile.observe(this, Observer { profileDB ->
            profileDB?.let { viewModel.initData(it) }
        })
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        EDIT_PROFILE,
        SETTINGS,
        LOG_OUT
    }
}