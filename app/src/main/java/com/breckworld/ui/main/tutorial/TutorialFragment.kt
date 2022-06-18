package com.breckworld.ui.main.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.activity.ActivityForFragments
import com.breckworld.activity.HomeActivity
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentTutorialBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.extensions.toast
import kotlinx.android.synthetic.main.fragment_tutorial.*

class TutorialFragment : BaseMVVMFragment<TutorialViewModel, FragmentTutorialBinding, TutorialFragment.Events>() {

    override fun viewModelClass(): Class<TutorialViewModel> = TutorialViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_tutorial

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.START_TUTORIAL ->  {
                    //findNavController().navigateTo(R.id.action_tutorialFragment_to_action_home)
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                }
                Events.OPEN_MAIN_SCREEN -> {
                    /*findNavController().navigateTo(R.id.action_tutorialFragment2_to_mainActivity)
                    activity?.finish()*/
                    val intent = Intent(activity, HomeActivity::class.java)
                    startActivity(intent)
                    activity?.finishAffinity()
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getBoolean(Constants.KEY_FIRST_RUN, false)?.let {
            viewModel.isFirstRun = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, false)
    }

    enum class Events {
        START_TUTORIAL,
        OPEN_MAIN_SCREEN
    }
}