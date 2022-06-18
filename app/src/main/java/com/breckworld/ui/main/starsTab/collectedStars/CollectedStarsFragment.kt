package com.breckworld.ui.main.starsTab.collectedStars

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.FragmentCollectedStarsBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import com.breckworld.repository.database.model.StarDB
import kotlinx.android.synthetic.main.fragment_collected_stars.*

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 22:15
 *         E-mail: bondes87@gmail.com
 */
class CollectedStarsFragment :
    BaseMVVMFragment<CollectedStarsViewModel, FragmentCollectedStarsBinding, CollectedStarsFragment.Events>() {

    override fun viewModelClass(): Class<CollectedStarsViewModel> = CollectedStarsViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_collected_stars

    private var collectedStarsRecyclerAdapter: CollectedStarsRecyclerAdapter? = null
    private var collectedStarsLayoutManager: LinearLayoutManager? = null

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.REFRESH_CLOSE -> swipe_refresh_layout_collected_stars.isRefreshing = false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.checkAndUpdateStars(false)
        viewModel.collectedStars.removeObservers(this)
        viewModel.collectedStars.observe(this, Observer {
            updateCollectedStarsRecyclerAdapter()
        })
    }

    private fun updateCollectedStarsRecyclerAdapter() {
        if (collectedStarsRecyclerAdapter == null) {
            initCollectedStarsRecyclerAdapter()
        } else {
            onCollectedStarsLoaded()
        }
    }

    private fun initViews() {
        collectedStarsLayoutManager = LinearLayoutManager(context)
        recycler_view_collected_stars?.layoutManager = collectedStarsLayoutManager
        recycler_view_collected_stars.addOnScrollListener(scrollListener)
        initCollectedStarsRecyclerAdapter()
        swipe_refresh_layout_collected_stars?.apply {
            setOnRefreshListener {
                viewModel.checkAndUpdateStars(true)
            }
            setColorSchemeResources(R.color.colorGreen)
        }
    }

    private fun initCollectedStarsRecyclerAdapter() {
        collectedStarsRecyclerAdapter =
            CollectedStarsRecyclerAdapter(viewModel.collectedStars.value?.toMutableList() ?: ArrayList(),
                object : BaseRecyclerViewAdapter.Listener<StarDB> {
                    override fun onItemClick(item: StarDB) {
                        val bundle = bundleOf(Constants.KEY_STAR to item)
                        findNavController().navigateTo(R.id.action_global_starInfoDialogFragment, bundle)
                    }
                })
        recycler_view_collected_stars?.adapter = collectedStarsRecyclerAdapter
    }

    private fun onCollectedStarsLoaded() {
        collectedStarsRecyclerAdapter?.replace(viewModel.collectedStars.value?.toMutableList())
        viewModel.isLoading.value = false
    }

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val defaultOffset = 16f
            var delta = 0f
            val position = collectedStarsLayoutManager?.findFirstVisibleItemPosition()
            if (position != null) {
                val view = collectedStarsLayoutManager?.findViewByPosition(position)
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

    enum class Events {
        REFRESH_CLOSE
    }
}