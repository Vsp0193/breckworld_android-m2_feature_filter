package com.breckworld.ui.main.settings

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentSettingsBinding
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_settings.*
import android.widget.Button
import android.widget.TextView
import com.breckworld.App


/**
 * @author Dmytro Bondarenko
 *         Date: 08.06.2019
 *         Time: 15:56
 *         E-mail: bondes87@gmail.com
 */
class SettingsFragment :
    BaseMVVMFragment<SettingsViewModel, FragmentSettingsBinding, SettingsFragment.Events>() {

    override fun viewModelClass(): Class<SettingsViewModel> = SettingsViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_settings

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.TERMS_OF_SERVICE -> findNavController().navigateTo(R.id.action_settingsFragment_to_termsOfServiceFragment2)
                Events.PRIVACY_POLICY -> findNavController().navigateTo(R.id.action_settingsFragment_to_privacyPolicyFragment2)
                Events.START_TUTORIAL -> showStartTutorialDialog()
                Events.REPORT_ISSUE -> findNavController().navigateTo(R.id.action_settingsFragment_to_reportIssueFragment)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, true)
        setupStatusBarPadding()
    }

    private fun showStartTutorialDialog() {
        val dialogBuilder = AlertDialog.Builder(context!!)
        dialogBuilder.setTitle(R.string.start_tutorial_title)
        dialogBuilder.setMessage(R.string.start_tutorial_message)
        dialogBuilder.setPositiveButton(R.string.yes) { _, _ ->
            findNavController().navigateTo(R.id.action_settingsFragment_to_tutorialFragment)
        }
        dialogBuilder.setNegativeButton(R.string.no) { dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
        val textView = alertDialog.window?.findViewById(android.R.id.message) as TextView
        val alertTitle = alertDialog.window?.findViewById(R.id.alertTitle) as TextView
        val button1 = alertDialog.window?.findViewById(android.R.id.button1) as Button
        val button2 = alertDialog.window?.findViewById(android.R.id.button2) as Button

        textView.typeface = App.getResFont(R.font.lato_regular)
        alertTitle.typeface = App.getResFont(R.font.lato_bold)
        button1.typeface = App.getResFont(R.font.lato_regular)
        button2.typeface = App.getResFont(R.font.lato_regular)
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    enum class Events {
        TERMS_OF_SERVICE,
        PRIVACY_POLICY,
        START_TUTORIAL,
        REPORT_ISSUE
    }
}