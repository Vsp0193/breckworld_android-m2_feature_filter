package com.breckworld.fragment

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import androidx.viewpager2.widget.ViewPager2.SCROLL_STATE_DRAGGING
import com.breckworld.App
import com.breckworld.R
import com.breckworld.activity.FilterActivity
import com.breckworld.activity.VisitActivity
import com.breckworld.adapter.*
import com.breckworld.app.repository.ResponseListener
import com.breckworld.databinding.FragmentHome1Binding
import com.breckworld.extensions.Constants
import com.breckworld.model.geostore.*
import com.breckworld.model.home.Slide
import com.breckworld.repository.Repository2
import com.breckworld.util.AppUtil
import com.breckworld.viewmodel.HomeVM
import com.breckworld.viewmodel.factory.HomeVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import java.util.*

class HomeFragment1 : Fragment(), LocationListener {

    var _binding: FragmentHome1Binding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeVM
    private var apiService: ApiInterface? = null
    var guideView: GuideView? = null

    private var fakeSize = 0
    private var realSize = 0
    private var currentPosition = 0
    private var itemList: ArrayList<Slide>? = null
    private var buttonClickedPosition = -1
    private var homeAdapter: HomeAdapter? = null

    var position = 0
    var layoutManager: LinearLayoutManager? = null
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var bannerListSize: Int? = null

    var dialog: Dialog? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var requestcode: Int = 101
    private var checkStatusFromFilter: Boolean = false

    // For search
    private var tabsArrayList: List<Tab>? = null
    private var bannersArrayList: List<Banner>? = null
    private var filtersArrayList: List<Filter>? = null
    private var sortArrayList: List<Sort>? = null
    private var colors: ColorsX? = null
    private var noDataText: String? = null
    private var selectedTabPosition = 0
    private var selectedSorting = ""
    private var selectedFilters = ""
    private var searchKeyword = ""
    private var loadMoreUrl = ""
    private var lat = ""
    private var lng = ""
    private var selectedFilterIds: ArrayList<Int> = ArrayList()
    private var defaultFilterIds: ArrayList<Int> = ArrayList()
    private var isLoadMoreAvailable: Boolean? = null
    private var geoStoreCategoryAdapter: GeoStoreCategoryAdapter? = null


    private val pageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPosition = position
            //onFocusChange()
        }

        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
            if (state == ViewPager2.SCROLL_STATE_IDLE) {
                try {
                    if (currentPosition === 0) {
                        binding.viewPagerVideos.setCurrentItem(fakeSize - 2, false)
                    } else if (currentPosition === fakeSize - 1) {
                        binding.viewPagerVideos.setCurrentItem(1, false)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (state == ViewPager2.SCROLL_STATE_DRAGGING && currentPosition === fakeSize) {
                //we scroll too fast and miss the state SCROLL_STATE_IDLE for the previous item
                binding.viewPagerVideos.setCurrentItem(2, false)
            }
        }
    }

    /**
     * add two lasts item at beginning on the new array
     * @param itemList
     * @return
     */
    fun transformListAndAddTwo(itemList: List<Slide>): ArrayList<Slide> {
        val size: Int = itemList.size
        val listTemp: ArrayList<Slide> = ArrayList(size + 2)
        for (iPL in 0..size + 2) {
            listTemp.add(itemList[(iPL + size - 2) % size])
        }
        return listTemp
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHome1Binding.inflate(inflater, container, false)

        apiService = ApiClient.createService(ApiInterface::class.java, requireContext())
        val repositories = Repository2(apiService!!, requireContext())
        homeViewModel = ViewModelProvider(
            this, HomeVMFactory(repositories)
        ).get(HomeVM::class.java)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        //CheckPermission()
        checkPermissionWithRequestPermission()

        /**
         * Call home api
         */
        homeViewModel.getHomeApiData(App.mLocalStore?.accessToken)

        homeViewModel.homeLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data != null) {

                        var slideList: List<Slide>? = null
                        for (i in it.data.content.data.buttons.indices) {
                            if (it.data.content.data.buttons.get(i).active) {
                                slideList =
                                    it.data.content.data.buttons.get(i).data.slides
                            }
                        }

                        // Filter List
                        filtersArrayList = it.data.content.data.filters

                        // Adding default filters
                        if (filtersArrayList != null) {
                            for (i in filtersArrayList?.indices!!) {
                                if (!defaultFilterIds.contains(filtersArrayList?.get(i)?.default!!)) {
                                    defaultFilterIds.add(filtersArrayList?.get(i)?.default!!)
                                }
                            }
                        }

                        // Sort List
                        sortArrayList = it.data.content.data.sort

                        // Manage sorting selection
                        for (i in sortArrayList?.indices!!) {
                            if (tabsArrayList?.get(selectedTabPosition)?.selectedSorting.equals("")) {
                                if (sortArrayList?.get(i)?.active!!) {
                                    tabsArrayList?.get(selectedTabPosition)?.selectedSorting =
                                        sortArrayList?.get(i)?.id.toString()

                                    selectedSorting =
                                        sortArrayList?.get(selectedTabPosition)?.id.toString()
                                }
                            } else {
                                tabsArrayList?.get(selectedTabPosition)?.selectedSorting =
                                    selectedSorting
                            }
                        }

                        // Set home adapter
                        homeAdapter = HomeAdapter(
                            requireContext(),
                            it.data.content.data.buttons,
                            slideList!!
                        )
                        binding.viewPagerVideos.orientation = ViewPager2.ORIENTATION_VERTICAL
                        binding.viewPagerVideos.adapter = homeAdapter
                        homeAdapter?.setOnClickListener(object :
                            HomeAdapter.OnClickListener {
                            override fun onVisitThisPageClickListener(position: Int) {
                                context?.startActivity(
                                    Intent(context, VisitActivity::class.java)
                                        .putExtra("cameFrom", "Home")
                                        .putExtra(
                                            "linkedMdsId",
                                            it.data.content.data.slides.get(position).ids.linked_mds
                                        )
                                )
                            }
                        })

                        // Set Buttons
                        val homeButtonsAdapter =
                            HomeButtonsAdapter(requireContext(), it.data.content.data.buttons)
                        binding.recyclerViewMDSButtons.layoutManager = LinearLayoutManager(context)
                        binding.recyclerViewMDSButtons.adapter = homeButtonsAdapter
                        homeButtonsAdapter.setOnClickListener(object :
                            HomeButtonsAdapter.OnClickListener {
                            override fun onButtonClickListener(
                                position: Int,
                                prevSelectedPosition: Int
                            ) {
                                realSize = 0
                                fakeSize = 0
                                if (prevSelectedPosition != position)
                                    homeAdapter?.updateNewData(position)

                                buttonClickedPosition = position
                                realSize =
                                    it.data.content.data.buttons.get(buttonClickedPosition).data.slides.size
                                fakeSize = realSize + 2;
                                itemList = transformListAndAddTwo(
                                    it.data.content.data.buttons.get(buttonClickedPosition).data.slides
                                )
                                homeAdapter?.updateList(itemList)
                                var item =
                                    2 //number two because we have two elements before the chosen one
                                binding.viewPagerVideos.setCurrentItem(item, false)
                            }
                        })

                        binding.viewPagerVideos.registerOnPageChangeCallback(pageChangeCallback)

                        if (buttonClickedPosition == -1) {
                            realSize = slideList.size
                            fakeSize = realSize + 2;
                            itemList = transformListAndAddTwo(slideList)
                            homeAdapter?.updateList(itemList)
                            var item =
                                2 //number two because we have two elements before the chosen one
                            binding.viewPagerVideos.setCurrentItem(item, false)
                        }

                        // Set banner adapter
                        val homeBannersAdapter =
                            HomeBannersAdapter(requireContext(), it.data.content.data.banners, null)
                        layoutManager = LinearLayoutManager(requireContext())
                        binding.recyclerViewBanners.layoutManager = layoutManager
                        binding.recyclerViewBanners.adapter = homeBannersAdapter
                        homeBannersAdapter.setOnClickListener(object :
                            HomeBannersAdapter.OnClickListener {
                            override fun onBannerClickListener(position: Int) {
                                context?.startActivity(
                                    Intent(context, VisitActivity::class.java)
                                        .putExtra("cameFrom", "Home")
                                        .putExtra(
                                            "linkedMdsId",
                                            it.data.content.data.banners.get(position).target.mds_id
                                        )
                                )
                            }
                        })

                        // Set banner scrolling
                        bannerListSize = it.data.content.data.banners.size
                        //position = Integer.MAX_VALUE / 2
                        position = 0
                        binding.recyclerViewBanners.scrollToPosition(position)

                        val snapHelper: SnapHelper = LinearSnapHelper()
                        snapHelper.attachToRecyclerView(binding.recyclerViewBanners)
                        binding.recyclerViewBanners.smoothScrollBy(5, 0)

                        binding.recyclerViewBanners.addOnScrollListener(object :
                            RecyclerView.OnScrollListener() {
                            override fun onScrollStateChanged(
                                @NonNull recyclerView: RecyclerView,
                                newState: Int
                            ) {
                                super.onScrollStateChanged(recyclerView, newState)
                                if (newState == 1) {
                                    //stopAutoScrollBanner()
                                } else if (newState == 0) {
                                    //position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                                    if (bannerListSize!! > 0)
                                        runAutoScrollBanner()
                                }
                            }
                        })

                        if (!App.mLocalStore?.isToolTipSeen!!)
                            showGuide()
                    }
                }
                is ResponseListener.Failure -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })

        binding.viewPagerVideos.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            private var fromPosition = 0
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                fromPosition = position
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == SCROLL_STATE_DRAGGING) {
                    homeAdapter?.setFadeIn(true, fromPosition)
                } else {
                    homeAdapter?.setFadeIn(false, fromPosition)
                }
                //homeAdapter?.notifyDataSetChanged()
            }
        })

        searchObserver()

        return binding.root
    }

    private fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask?.cancel()
            timer?.cancel()
            timer = null
            timerTask = null
            position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
        }
    }

    private fun runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (position == Int.MAX_VALUE) {
                        position = Int.MAX_VALUE / 2
                        binding.recyclerViewBanners.scrollToPosition(position)
                        binding.recyclerViewBanners.smoothScrollBy(0, 5)
                    } else {
                        position++
                        binding.recyclerViewBanners.smoothScrollToPosition(position)

                        if (position == bannerListSize) {
                            binding.recyclerViewBanners.smoothScrollToPosition(0)
                            position = 0
                        }
                    }
                }
            }
            timer?.schedule(timerTask, 5000, 5000)
        }
    }

    fun showGuide() {
        Handler().postDelayed({
            vrplayerGuide()
        }, Constants.GUIDE_DELAY)
    }

    private fun vrplayerGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_home_1))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(_binding?.recyclerViewBanners)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    App.mLocalStore?.saveToolTipSeen(true)
                    //vrplayerGuideContinue()
                }
            }
            .build()
        guideView?.show()
    }

    override fun onResume() {
        super.onResume()
        runAutoScrollBanner()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner()
    }

    fun getSearch(keyword: String) {
        searchKeyword = keyword
        //Toast.makeText(context, searchKeyword, Toast.LENGTH_SHORT).show()
        _binding?.layoutHome?.visibility = View.GONE
        _binding?.layoutHomeSearch?.visibility = View.VISIBLE
        homeViewModel.searchApiData(
            "",
            searchKeyword,
            selectedFilters,
            selectedSorting,
            lat,
            lng,
            App.mLocalStore?.accessToken
        )
        //searchObserver()
    }

    fun rwsClicked() {
        _binding?.layoutHome?.visibility = View.GONE
        _binding?.layoutHomeSearch?.visibility = View.VISIBLE
        homeViewModel.searchApiData(
            "1",
            "",
            selectedFilters,
            selectedSorting,
            lat,
            lng,
            App.mLocalStore?.accessToken
        )
    }

    fun stackClicked() {
        if (tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds != null &&
            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds?.size!! > 0
        ) {
            startActivityForResult(
                Intent(requireActivity(), FilterActivity::class.java)
                    .putParcelableArrayListExtra(
                        "filters",
                        filtersArrayList as ArrayList<out Parcelable>
                    )
                    .putExtra(
                        "selectedIds",
                        tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds
                    ), GeoStoreFragment.REQUEST_CODE
            )
        } else {
            startActivityForResult(
                Intent(requireActivity(), FilterActivity::class.java)
                    .putParcelableArrayListExtra(
                        "filters",
                        filtersArrayList as ArrayList<out Parcelable>
                    )
                    .putExtra(
                        "selectedIds",
                        defaultFilterIds
                    ), GeoStoreFragment.REQUEST_CODE
            )
        }
    }

    fun backClicked() {
        checkStatusFromFilter = false
        _binding?.layoutHome?.visibility = View.VISIBLE
        _binding?.layoutHomeSearch?.visibility = View.GONE
    }

    fun searchObserver() {
        homeViewModel.searchLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data != null) {

                        // Set background
                        if (!it.data.content.data.images.list_bg.equals("")) {
                            Glide.with(requireContext()).load(it.data.content.data.images.list_bg)
                                .into(_binding?.imageViewBg!!)
                        } else {
                            _binding?.imageViewBg?.setBackgroundDrawable(
                                ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.filter_bg
                                )
                            )
                        }

                        // No Data Text
                        noDataText = it.data.content.data.labels.no_results

                        // Banner List
                        bannersArrayList = it.data.content.data.banners

                        // Colors
                        colors = it.data.content.data.colors

                        // Tab List
                        if (tabsArrayList == null) {
                            tabsArrayList = it.data.content.data.tabs
                        } else {
                            //tabsArrayList?.toMutableList()?.set(selectedTabPosition, it.data.content.data.tabs.get(selectedTabPosition))
                            tabsArrayList?.toMutableList()?.get(selectedTabPosition)?.cards =
                                it.data.content.data.tabs.get(selectedTabPosition).cards
                        }

                        if (selectedFilterIds.size > 0) {
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds =
                                selectedFilterIds
                        } else {
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds =
                                defaultFilterIds
                        }

                        if (it.data.content.data.tabs.get(selectedTabPosition).load_more.equals(""))
                            isLoadMoreAvailable = false
                        else {
                            isLoadMoreAvailable = true
                            loadMoreUrl =
                                it.data.content.data.tabs.get(selectedTabPosition).load_more
                        }

                        // Filter List
                        filtersArrayList = it.data.content.data.filters

                        // Adding default filters
                        if (filtersArrayList != null) {
                            for (i in filtersArrayList?.indices!!) {
                                if (!defaultFilterIds.contains(filtersArrayList?.get(i)?.default!!)) {
                                    defaultFilterIds.add(filtersArrayList?.get(i)?.default!!)
                                }
                            }
                        }

                        // Sort List
                        sortArrayList = it.data.content.data.sort

                        // Manage sorting selection
                        for (i in sortArrayList?.indices!!) {
                            if (tabsArrayList?.get(selectedTabPosition)?.selectedSorting.equals("")) {
                                if (sortArrayList?.get(i)?.active!!) {
                                    tabsArrayList?.get(selectedTabPosition)?.selectedSorting =
                                        sortArrayList?.get(i)?.id.toString()

                                    selectedSorting =
                                        sortArrayList?.get(selectedTabPosition)?.id.toString()
                                }
                            } else {
                                tabsArrayList?.get(selectedTabPosition)?.selectedSorting =
                                    selectedSorting
                            }
                        }

                        // Update badge count on tabs
                        if (tabsArrayList?.get(selectedTabPosition)?.selectedSorting != null &&
                            !tabsArrayList?.get(selectedTabPosition)?.selectedSorting.equals("") ||
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds != null &&
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds?.size!! > 0
                        ) {
                            if (geoStoreCategoryAdapter == null) {
                                setDataToAdapterForCategory()
                            }
                            geoStoreCategoryAdapter?.updateCount(
                                selectedTabPosition,
                                it.data.content.data.tabs.get(selectedTabPosition).tab_count
                            )
                        } else {
                            setDataToAdapterForCategory()
                        }

                        setDataToAdapterForBanners()
                        setDataToAdapterForGeoStoreContent()

                        binding.layoutFilterButtons.visibility = View.VISIBLE

                    }
                }
                is ResponseListener.Failure -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })

        setObserverForLoadMoreData()

        binding.imageViewFilter.setOnClickListener {

            if (tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds != null &&
                tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds?.size!! > 0
            ) {
                startActivityForResult(
                    Intent(requireActivity(), FilterActivity::class.java)
                        .putParcelableArrayListExtra(
                            "filters",
                            filtersArrayList as ArrayList<out Parcelable>
                        )
                        .putExtra(
                            "selectedIds",
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds
                        ), GeoStoreFragment.REQUEST_CODE
                )
            } else {
                startActivityForResult(
                    Intent(requireActivity(), FilterActivity::class.java)
                        .putParcelableArrayListExtra(
                            "filters",
                            filtersArrayList as ArrayList<out Parcelable>
                        )
                        .putExtra(
                            "selectedIds",
                            defaultFilterIds
                        ), GeoStoreFragment.REQUEST_CODE
                )
            }
        }

        binding.imageViewSort.setOnClickListener {
            showFiltersDialog(tabsArrayList?.get(selectedTabPosition)?.selectedSorting.toString())
        }

    }

    fun setObserverForLoadMoreData() {
        homeViewModel.geoStoreLoadMoreLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResponseListener.Success -> {
                    if (it.data != null) {

                        // Tab List
                        tabsArrayList?.get(selectedTabPosition)?.cards =
                            tabsArrayList?.get(selectedTabPosition)?.cards?.plus(
                                it.data.content.data.tabs.get(selectedTabPosition).cards
                            )!!

                        if (it.data.content.data.tabs.get(selectedTabPosition).load_more.equals("")) {
                            isLoadMoreAvailable = false
                            homeViewModel.geoStoreLoadMoreLiveData.removeObservers(
                                viewLifecycleOwner
                            )
                        } else {
                            isLoadMoreAvailable = true
                            loadMoreUrl =
                                it.data.content.data.tabs.get(selectedTabPosition).load_more
                        }

                        setDataToAdapterForGeoStoreContent()
                        _binding?.recyclerViewGeoStore?.layoutManager?.scrollToPosition(
                            tabsArrayList?.get(selectedTabPosition)?.cards?.size!! - (it.data.content.data.tabs.get(
                                selectedTabPosition
                            ).cards.size + 1)
                        )
                    }
                }
                is ResponseListener.Failure -> {
                    homeViewModel.geoStoreLoadMoreLiveData.removeObservers(viewLifecycleOwner)
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    homeViewModel.geoStoreLoadMoreLiveData.removeObservers(viewLifecycleOwner)
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun showFiltersDialog(selectedSort: String) {
        try {
            val filterOption = BottomSheetDialog(requireContext())
            val sheetView: View = layoutInflater.inflate(R.layout.layout_sorting, null)
            val recyclerViewFilter: RecyclerView = sheetView.findViewById(R.id.recyclerViewSorting)
            val optionCancel = sheetView.findViewById<TextView>(R.id.optionCancel)
            val filterResetTips = sheetView.findViewById<TextView>(R.id.filterResetTips)
            filterOption.setContentView(sheetView)

            // Set data to adapter
            val adapter = SortingAdapter(requireContext(), sortArrayList!!, selectedSort)
            val layoutManager = LinearLayoutManager(context)
            recyclerViewFilter.layoutManager = layoutManager
            recyclerViewFilter.adapter = adapter
            adapter.setOnClickListener(object : SortingAdapter.OnClickListener {
                override fun onSortingClickListener(position: Int) {
                    selectedSorting = sortArrayList?.get(position)?.id.toString()
                    homeViewModel.searchApiData(
                        "",
                        searchKeyword,
                        selectedFilters,
                        selectedSorting,
                        lat,
                        lng,
                        App.mLocalStore?.accessToken
                    )
                    filterOption.cancel()
                }

            })
            filterResetTips.setOnClickListener { v: View? ->
                filterOption.cancel()
            }
            optionCancel.setOnClickListener { view: View? -> filterOption.cancel() }
            filterOption.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun setDataToAdapterForBanners() {
        val homeBannersAdapter = HomeBannersAdapter(requireContext(), null, bannersArrayList)
        layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewBanners.layoutManager = layoutManager
        binding.recyclerViewBanners.adapter = homeBannersAdapter
        homeBannersAdapter.setOnClickListener(object :
            HomeBannersAdapter.OnClickListener {
            override fun onBannerClickListener(position: Int) {
                requireActivity().startActivity(
                    Intent(context, VisitActivity::class.java)
                        .putExtra("cameFrom", "GeoStore")
                        .putExtra(
                            "linkedMdsId",
                            bannersArrayList?.get(position)?.target?.mds_id
                        )
                )
            }
        })

        // Set banner scrolling
        bannerListSize = bannersArrayList?.size
        position = 0
        binding.recyclerViewBanners.scrollToPosition(position)

        val snapHelper: SnapHelper = LinearSnapHelper()
        try {
            snapHelper.attachToRecyclerView(binding.recyclerViewBanners)
            binding.recyclerViewBanners.smoothScrollBy(5, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.recyclerViewBanners.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                @NonNull recyclerView: RecyclerView,
                newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1) {
                    //stopAutoScrollBanner()
                } else if (newState == 0) {
                    //position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
                    if (bannerListSize!! > 0)
                        runAutoScrollBanner()
                }
            }
        })

    }

    fun setDataToAdapterForCategory() {
        tabsArrayList?.get(selectedTabPosition)?.isSelected = true
        geoStoreCategoryAdapter = GeoStoreCategoryAdapter(requireContext(), tabsArrayList!!, colors)

        _binding?.recyclerViewCategory?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        _binding?.recyclerViewCategory?.adapter = geoStoreCategoryAdapter
        geoStoreCategoryAdapter?.setOnClickListener(object :
            GeoStoreCategoryAdapter.OnClickListener {
            override fun onCategoryClickListener(position: Int) {
                selectedTabPosition = position

                if (tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds != null) {
                    selectedFilters =
                        AppUtil.convertIntArrayToCommaSeparated(
                            tabsArrayList?.get(
                                selectedTabPosition
                            )?.selectedFilterIds!!
                        )
                    if (tabsArrayList?.get(selectedTabPosition)?.selectedSorting != null)
                        selectedSorting = tabsArrayList?.get(selectedTabPosition)?.selectedSorting!!
                }
                setDataToAdapterForGeoStoreContent()
            }
        })
    }

    fun setDataToAdapterForGeoStoreContent() {
        if (tabsArrayList?.get(selectedTabPosition)?.cards != null && tabsArrayList?.get(
                selectedTabPosition
            )?.cards?.size!! > 0
        ) {
            _binding?.recyclerViewGeoStore?.visibility = View.VISIBLE
            _binding?.layoutNoData?.visibility = View.GONE

            setDataForItem(tabsArrayList?.get(selectedTabPosition)?.cards!!)

        } else {
            _binding?.layoutNoData?.visibility = View.VISIBLE
            _binding?.recyclerViewGeoStore?.visibility = View.GONE
            _binding?.textViewNoData?.text = noDataText
        }
    }

    private fun setDataForItem(cardList: List<Card>) {
        var geoStoreAdapter = GeoStoreAdapter(requireContext(), cardList)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        _binding?.recyclerViewGeoStore?.setLayoutManager(mLayoutManager)
        _binding?.recyclerViewGeoStore?.adapter = geoStoreAdapter
        geoStoreAdapter.setOnClickListener(object :
            GeoStoreAdapter.OnClickListener {
            override fun onCategoryClickListener(position: Int) {
                context?.startActivity(
                    Intent(context, VisitActivity::class.java)
                        .putExtra("cameFrom", "GeoStore")
                        .putExtra(
                            "linkedMdsId",
                            tabsArrayList?.get(selectedTabPosition)?.cards?.get(position)?.ids?.mds
                        )
                        .putExtra(
                            "slideId",
                            tabsArrayList?.get(selectedTabPosition)?.cards?.get(position)?.ids?.slide
                        )
                )
            }

            override fun ontextViewDistanceClickListener(position: Int) {
                if (CheckPermission() && isLocationEnabled()) {
                    if (lat.equals("") && lng.equals("")) {
                        Toast.makeText(
                            requireContext(),
                            "Please wait we are getting your current location.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    setLocation()
                } else {
                    checkPermissionWithRequestPermission()
                }
            }
        })

        _binding?.recyclerViewGeoStore?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isLoadMoreAvailable!! && tabsArrayList?.get(selectedTabPosition)?.cards?.size!! < tabsArrayList?.get(
                            selectedTabPosition
                        )?.tab_count!!
                    ) {
                        val url = loadMoreUrl.split(ApiClient.BASE_SERVER_URL)
                        homeViewModel.searchLoadMoreApiData(url.get(1))
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GeoStoreFragment.REQUEST_CODE && resultCode == GeoStoreFragment.RESULT_CODE) {
            selectedFilters = ""
            checkStatusFromFilter = true
            val testResult = data?.getIntegerArrayListExtra(GeoStoreFragment.EXTRA_KEY_TEST)
            setObserverForLoadMoreData()
            if (testResult != null && testResult.size > 0) {
                selectedFilterIds = testResult
                selectedFilters = AppUtil.convertIntArrayToCommaSeparated(testResult)

            } else {
                selectedFilterIds.clear()
            }

            homeViewModel.searchApiData(
                "",
                searchKeyword,
                selectedFilters,
                selectedSorting,
                lat,
                lng,
                App.mLocalStore?.accessToken
            )
        }
    }

    fun CheckPermission(): Boolean {
        if (
            activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED ||
            activity?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }

        return false

    }

    fun RequestPermission() {
        activity?.let {
            ActivityCompat.requestPermissions(
                it,
                arrayOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ),
                requestcode

            )
        }
    }

    /*
    * Main method to capture location
    * */
    fun getLastLocation() {
        if (isLocationEnabled()) {
            locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if ((ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED)
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    locationPermissionCode
                )
            }
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                3000,
                5f,
                this
            )
            homeViewModel.searchApiData(
                "",
                searchKeyword,
                selectedFilters,
                selectedSorting,
                lat,
                lng,
                App.mLocalStore?.accessToken
            )

        } else {
            showConfirmationDialog(requireContext())
        }
    }

    /*
    * Code to check location enable or not
    * */
    fun isLocationEnabled(): Boolean {
        var locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    /*
    * Show custom dialog to move from location enable setting
    * */
    fun showConfirmationDialog(context: Context) {
        dialog = Dialog(requireContext())
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.item_permission)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val tv_detail = dialog?.findViewById(R.id.tv_detail) as AppCompatTextView
        val tvTitle = dialog?.findViewById(R.id.tvTitle) as AppCompatTextView
        val tv_edit = dialog?.findViewById(R.id.tv_edit) as TextView
        val tv_ok = dialog?.findViewById(R.id.tv_ok) as TextView
        tvTitle.text = getString(R.string.alert_title)
        //tvTitle.setTextColor(Color.parseColor("#8F2927"))
        tv_detail.text = getString(R.string.location_permission_description) //  location_enable
        tv_ok.text = getString(R.string.ok)
        tv_edit.text = getString(R.string.cancel)

        tv_ok.setOnClickListener {
            dialog?.dismiss()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
        tv_edit.setOnClickListener {
            dialog?.dismiss()
            homeViewModel.searchApiData(
                "",
                searchKeyword,
                selectedFilters,
                selectedSorting,
                lat,
                lng,
                App.mLocalStore?.accessToken
            )
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

    }

    /*
    * method to check all condition for location capturing
    * */
    fun setLocation() {

        if (checkStatusFromFilter == true) {
            if (CheckPermission()) {
                if (!CheckPermission()) {
                    RequestPermission()
                } else if (!isLocationEnabled()) {
                    showConfirmationDialog(requireContext())
                } else if (isLocationEnabled() == true && CheckPermission() == true) {
                    getLastLocation()
                }


            } else {
                homeViewModel.searchApiData(
                    "",
                    searchKeyword,
                    selectedFilters,
                    selectedSorting,
                    lat,
                    lng,
                    App.mLocalStore?.accessToken
                )

            }
        } else {

        }
    }

    fun checkPermissionWithRequestPermission() {
        if (!CheckPermission()) {
            RequestPermission()
        } else if (CheckPermission() && !isLocationEnabled()) {
            showConfirmationDialog(requireContext())
        }

    }

    /*
    * Listner for change location and capture latlong
    * */
    override fun onLocationChanged(location: Location) {
        lat = location.latitude.toString()
        lng = location.longitude.toString()
        /*if (counter == 0)
         {
             geoStoreViewModel.getGeoStoreApiData(
                 selectedSorting,
                 selectedFilters,
                 lat, lng,
                 App.mLocalStore?.accessToken)
               counter++
         }*/
    }

}