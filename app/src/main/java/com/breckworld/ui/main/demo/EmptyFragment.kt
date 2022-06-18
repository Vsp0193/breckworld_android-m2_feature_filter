package com.breckworld.ui.main.demo

import android.os.Bundle
import android.view.View
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.databinding.FragmentEmptyBinding
import com.breckworld.livedata.EventObserver
import kotlinx.android.synthetic.main.fragment_settings.*

class EmptyFragment : BaseMVVMFragment<EmptyViewModel, FragmentEmptyBinding, EmptyFragment.Events>() {
    override fun viewModelClass(): Class<EmptyViewModel> {
        return EmptyViewModel::class.java
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_empty
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(true)
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

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.EXIT -> {
                    viewModel.logout()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
    }

    enum class Events {
        BACK,
        EXIT
    }

    companion object {
        fun newInstance(args: Bundle? = null): EmptyFragment {
            val fragment = EmptyFragment()
            fragment.arguments = args
            return fragment
        }
    }
}