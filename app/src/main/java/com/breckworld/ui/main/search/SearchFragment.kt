package com.breckworld.ui.main.search

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.FragmentSearchBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import com.breckworld.location.CurrentLocationLiveData
import com.breckworld.repository.local.model.CityModel
import kotlinx.android.synthetic.main.fragment_search.*
import permissions.dispatcher.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity

/**
 * @author Dmytro Bondarenko
 *         Date: 04.06.2019
 *         Time: 16:19
 *         E-mail: bondes87@gmail.com
 */
@RuntimePermissions
class SearchFragment : BaseMVVMFragment<SearchViewModel, FragmentSearchBinding, SearchFragment.Events>() {

    override fun viewModelClass(): Class<SearchViewModel> = SearchViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_search

    private var citiesRecyclerAdapter: CitiesRecyclerAdapter? = null

    var guideView: GuideView? = null

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.GO_TO_SEARCH_RESULT -> {
                    val bundle = bundleOf(Constants.KEY_SEARCH_MODEL to viewModel.createSearchModel())
                    findNavController().navigateTo(R.id.action_action_search_to_searchResultsFragment, bundle)
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableLocationWithPermissionCheck()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        showBottomNavigation(true)
        setupStatusbar(true, true)
        setupStatusBarPadding()
        showGuide()
        val search = arguments?.getInt(Constants.KEY_SEARCH, 0)
        if (search == Constants.SEARCH_FUN) {
            setupFilter(search)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        guideView?.dismiss()
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
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
                viewModel.isSearchButtonEnabled.value = true
            })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun initViews() {
        recycler_view_cities?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        citiesRecyclerAdapter = CitiesRecyclerAdapter(viewModel.cities, viewModel.selectedCityPosition,
            object : BaseRecyclerViewAdapter.Listener<CityModel> {
                override fun onItemClick(item: CityModel) {
                    val itemPosition = citiesRecyclerAdapter?.getItemPosition(item)
                    viewModel.selectedCityPosition = if (itemPosition == null || itemPosition == -1) {
                        0
                    } else {
                        itemPosition
                    }
                    citiesRecyclerAdapter?.changeSelected(itemPosition!!)
                }
            })
        recycler_view_cities?.adapter = citiesRecyclerAdapter
    }

    private fun setupFilter(filterType: Int) {
        when (filterType) {
            Constants.SEARCH_FUN -> viewModel.freeFun.value = true
        }
    }

    private fun showGuide() {
        if (viewModel.isGuideSearch()) {
            Handler().postDelayed({
                selectCityGuide()
            }, Constants.GUIDE_DELAY)
        }
    }

    private fun selectCityGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_search_1))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_city)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    attractionsGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun attractionsGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_search_2))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_attractions)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    freeFunGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun freeFunGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_search_3))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_free_fun)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    eatAndDrinkGuide()
                }
            }
            .build()
        guideView?.show()
    }

    private fun eatAndDrinkGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_search_3))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_eat_and_drink)
            .setDismissType(DismissType.insideMessageView) //optional - default dismissible by TargetView
            .setGuideListener {
                if (isVisible) {
                    viewModel.saveGuideSearch(false)
                }
            }
            .build()
        guideView?.show()
    }

    enum class Events {
        GO_TO_SEARCH_RESULT
    }
}