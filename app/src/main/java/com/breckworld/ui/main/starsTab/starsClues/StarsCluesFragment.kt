package com.breckworld.ui.main.starsTab.starsClues

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.FragmentStarsCluesBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import com.breckworld.location.CurrentLocationLiveData
import com.breckworld.repository.database.model.LocationStarDB
import kotlinx.android.synthetic.main.fragment_stars_clues.*
import permissions.dispatcher.*

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 13:18
 *         E-mail: bondes87@gmail.com
 */

@RuntimePermissions
class StarsCluesFragment :
    BaseMVVMFragment<StarsCluesViewModel, FragmentStarsCluesBinding, StarsCluesFragment.Events>() {

    override fun viewModelClass(): Class<StarsCluesViewModel> = StarsCluesViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_stars_clues

    private var starsClueRecyclerAdapter: StarsClueRecyclerAdapter? = null
    private var starsClueLayoutManager: LinearLayoutManager? = null

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.REFRESH_CLOSE -> swipe_refresh_layout_stars_clues.isRefreshing = false
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(true)
        setupStatusbar(true, true)
        initViews()
        enableLocationWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        CurrentLocationLiveData.getInstance().observe(this,
            Observer<Location> { location ->
                viewModel.currentLocation = location
                viewModel.checkAndUpdateStars(false)
            })
        viewModel.starsClues.removeObservers(this)
        viewModel.starsClues.observe(this, Observer {
            updateStarsClueRecyclerAdapter()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun updateStarsClueRecyclerAdapter() {
        if (starsClueRecyclerAdapter == null) {
            initStarsClueRecyclerAdapter()
        } else {
            onStarsCluesLoaded()
        }
    }

    private fun onStarsCluesLoaded() {
        starsClueRecyclerAdapter?.replace(viewModel.starsClues.value?.toMutableList())
        viewModel.isLoading.value = false
    }

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val defaultOffset = 16f
            var delta = 0f
            val position = starsClueLayoutManager?.findFirstVisibleItemPosition()
            if (position != null) {
                val view = starsClueLayoutManager?.findViewByPosition(position)
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

    private fun initViews() {
        starsClueLayoutManager = LinearLayoutManager(context)
        recycler_view_stars_clues?.layoutManager = starsClueLayoutManager
        recycler_view_stars_clues.addOnScrollListener(scrollListener)
        initStarsClueRecyclerAdapter()
        swipe_refresh_layout_stars_clues?.apply {
            setOnRefreshListener {
                viewModel.checkAndUpdateStars(true)
            }
            setColorSchemeResources(R.color.colorGreen)
        }
    }

    private fun initStarsClueRecyclerAdapter() {
        starsClueRecyclerAdapter =
            StarsClueRecyclerAdapter(viewModel.starsClues.value?.toMutableList() ?: ArrayList(),
                object : BaseRecyclerViewAdapter.Listener<LocationStarDB> {
                    override fun onItemClick(item: LocationStarDB) {
                        val bundle = bundleOf(Constants.KEY_STAR to item)
                        findNavController().navigateTo(R.id.action_global_starInfoDialogFragment, bundle)
                    }
                })
        recycler_view_stars_clues?.adapter = starsClueRecyclerAdapter
    }

    enum class Events {
        REFRESH_CLOSE
    }
}