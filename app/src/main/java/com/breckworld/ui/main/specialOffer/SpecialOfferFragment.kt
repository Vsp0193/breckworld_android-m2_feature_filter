package com.breckworld.ui.main.specialOffer

import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentSpecialOfferBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.repository.database.model.SpecialOfferDB
import com.breckworld.ui.main.specialOffer.adapter.SpecialOfferRvAdapter
import kotlinx.android.synthetic.main.fragment_special_offer.*
import kotlinx.android.synthetic.main.fragment_special_offer.fl_dialog

class SpecialOfferFragment :
    BaseMVVMFragment<SpecialOfferViewModel, FragmentSpecialOfferBinding, SpecialOfferFragment.Events>() {

    override fun viewModelClass(): Class<SpecialOfferViewModel> = SpecialOfferViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_special_offer

    private val specialOfferRvAdapter = SpecialOfferRvAdapter(mutableListOf(), object : SpecialOfferRvAdapter.Listener<SpecialOfferDB> {
        override fun onItemClick(item: SpecialOfferDB) {
            //viewModel.collectSpecialOffer(item)
            val bundle = bundleOf(Constants.KEY_OFFER to item, Constants.KEY_LOCATION to viewModel.location)
            findNavController().navigateTo(R.id.action_specialOffer_to_OfferDialogFragment, bundle)
        }
    })

    private var specialOfferLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            viewModel.location = getParcelable(Constants.KEY_LOCATION) as? Location
        }
        viewModel.specialOffers.observe(this, Observer {
            specialOfferRvAdapter.replace(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, false)
        setupStatusBarPadding(true)
        initSpecialOfferRecycler()
        viewModel.checkAndUpdateSpecialOffers(true)
    }

    fun setupStatusBarPadding(enablePadding: Boolean) {
        val top = if (enablePadding) getStatusBarHeight() else 0
        fl_dialog.setPadding(0, top, 0, 0)
    }

    private fun initSpecialOfferRecycler() {
        specialOfferLayoutManager = LinearLayoutManager(context)
        rv_special_offer.adapter = specialOfferRvAdapter
        rv_special_offer.layoutManager = specialOfferLayoutManager
        rv_special_offer.addOnScrollListener(scrollListener)
    }

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val defaultOffset = 16f
            var delta = 0f
            val position = specialOfferLayoutManager?.findFirstVisibleItemPosition()
            if (position != null) {
                val view = specialOfferLayoutManager?.findViewByPosition(position)
                if (view != null) {
                    if (position == 0) {
                        delta = -view.top / defaultOffset
                        if (delta > 1f) delta = 1f
                        if (delta < 0f) delta = 0f
                    } else {
                        delta = 1f
                    }
                }
            }
            recycler_view_separator.alpha = delta
        }
    }


    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
                Events.SPECIAL_OFFER_COLLECTED -> findNavController().navigateUp()
            }
        })
    }

    enum class Events {
        BACK,
        SPECIAL_OFFER_COLLECTED
    }

    companion object {
        fun newInstance(args: Bundle? = null): SpecialOfferFragment {
            val fragment = SpecialOfferFragment()
            fragment.arguments = args
            return fragment
        }
    }

}