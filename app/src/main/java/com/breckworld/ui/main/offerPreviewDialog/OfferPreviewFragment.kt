package com.breckworld.ui.main.offerPreviewDialog

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentOfferPreviewBinding
import com.breckworld.extensions.Constants
import com.breckworld.repository.database.model.OfferDB
import com.travijuu.numberpicker.library.Interface.ValueChangedListener
import kotlinx.android.synthetic.main.fragment_offer_preview.*

class OfferPreviewFragment :
    BaseMVVMFragment<OfferPreviewViewModel, FragmentOfferPreviewBinding, OfferPreviewFragment.Events>() {

    override fun viewModelClass(): Class<OfferPreviewViewModel> = OfferPreviewViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_offer_preview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            (getSerializable(Constants.KEY_OFFER) as OfferDB?)?.let {
                viewModel.initData(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, false)
        setupStatusBarPadding()
        initViews()
    }

    private fun initViews() {
       // ((number_picker_adults?.getChildAt(0) as ViewGroup ).getChildAt(0) as Button).setBackgroundColor(ContextCompat.getColor(context!!, R.color.colorWhite))
        number_picker_adults?.valueChangedListener =
            ValueChangedListener { value, _ -> viewModel.adults.value = value }

        number_picker_children?.valueChangedListener =
            ValueChangedListener { value, _ -> viewModel.children.value = value }
    }

    fun setupStatusBarPadding() {
        val top = constraint_layout_dialog.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_dialog.paddingBottom
        val start = constraint_layout_dialog.paddingStart
        val end = constraint_layout_dialog.paddingEnd
        constraint_layout_dialog.setPadding(start, top, end, bottom)
    }

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
            }
        })
    }

    enum class Events {
        BACK
    }
}