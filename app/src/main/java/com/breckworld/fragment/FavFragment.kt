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
import android.net.Uri
import android.os.Bundle
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.*
import com.breckworld.App
import com.breckworld.R
import com.breckworld.activity.FilterActivity
import com.breckworld.activity.VisitActivity
import com.breckworld.adapter.GeoStoreAdapter
import com.breckworld.adapter.GeoStoreCategoryAdapter
import com.breckworld.adapter.HomeBannersAdapter
import com.breckworld.adapter.SortingAdapter
import com.breckworld.app.repository.ResponseListener
import com.breckworld.databinding.FragmentFavBinding
import com.breckworld.model.geostore.*
import com.breckworld.repository.Repository2
import com.breckworld.viewmodel.FavoriteTabVM
import com.breckworld.viewmodel.factory.FavoriteTabVMFactory
import com.breckworld.webservice.ApiClient
import com.breckworld.webservice.ApiInterface
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

class FavFragment : Fragment(), LocationListener {

    var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private lateinit var favTabViewModel: FavoriteTabVM
    private var apiService: ApiInterface? = null
    var layoutManager: LinearLayoutManager? = null
    private var geoStoreCategoryAdapter: GeoStoreCategoryAdapter? = null

    private var tabsArrayList: List<Tab>? = null
    private var bannersArrayList: List<Banner>? = null
    private var filtersArrayList: List<Filter>? = null
    private var sortArrayList: List<Sort>? = null
    private var colors: ColorsX? = null
    private var noDataText: String? = null
    private var selectedTabPosition = 0
    private var selectedSorting = ""
    private var selectedFilters = ""
    private var loadMoreUrl = ""
    private var searchKeyword = ""
    private var lat = ""
    private var lng = ""
    private var checkStatusFromFilter :Boolean = false

    private var defaultFilterIds: ArrayList<Int> = ArrayList()
    private var selectedFilterIds: ArrayList<Int> = ArrayList()
    private var isLoadMoreAvailable: Boolean? = null
    private var isSearchAvailable = false
    private var dialog :Dialog?= null

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    var requestcode :Int =101

    var position = 0
    var timer: Timer? = null
    var timerTask: TimerTask? = null
    var bannerListSize: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        apiService = ApiClient.createService(ApiInterface::class.java, requireContext())
        val repositories = Repository2(apiService!!, requireContext())
        favTabViewModel = ViewModelProvider(
            this, FavoriteTabVMFactory(repositories)
        ).get(FavoriteTabVM::class.java)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        checkPermissionWithRequestPermission()

        /**
         * Observe favourite api
         */
        favTabViewModel.favouriteTabLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                        } /*else {
                            tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds = defaultFilterIds
                        }*/

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
        searchObserver()

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

        return binding.root
    }

    fun setObserverForLoadMoreData() {
        favTabViewModel.favouriteTabLoadMoreLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
                            favTabViewModel.favouriteTabLoadMoreLiveData.removeObservers(
                                viewLifecycleOwner
                            )
                        } else {
                            isLoadMoreAvailable = true
                            loadMoreUrl =
                                it.data.content.data.tabs.get(selectedTabPosition).load_more
                        }

                        setDataToAdapterForGeoStoreContent()
                        _binding?.recyclerViewFav?.layoutManager?.scrollToPosition(
                            tabsArrayList?.get(selectedTabPosition)?.cards?.size!! - (it.data.content.data.tabs.get(
                                selectedTabPosition
                            ).cards.size + 1)
                        )
                    }
                }
                is ResponseListener.Failure -> {
                    favTabViewModel.favouriteTabLoadMoreLiveData.removeObservers(viewLifecycleOwner)
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
                is ResponseListener.Error -> {
                    favTabViewModel.favouriteTabLoadMoreLiveData.removeObservers(viewLifecycleOwner)
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
                    if(!isSearchAvailable) {
                        favTabViewModel.getFavoriteTabApiData(
                            selectedSorting,
                            selectedFilters,
                            lat, lng,
                            App.mLocalStore?.accessToken
                        )
                    } else {
                        favTabViewModel.searchApiData(
                            "",
                            searchKeyword,
                            selectedFilters,
                            selectedSorting,
                            lat,
                            lng,
                            App.mLocalStore?.accessToken
                        )
                    }
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
                        convertIntArrayToCommaSeparated(tabsArrayList?.get(selectedTabPosition)?.selectedFilterIds!!)
                    if (tabsArrayList?.get(selectedTabPosition)?.selectedSorting != null)
                        selectedSorting = tabsArrayList?.get(selectedTabPosition)?.selectedSorting!!
                } /*else {
                    selectedFilters = ""
                    selectedFilterIds.clear()
                }*/
                setDataToAdapterForGeoStoreContent()
            }
        })
    }

    fun setDataToAdapterForGeoStoreContent() {
        if (tabsArrayList?.get(selectedTabPosition)?.cards != null && tabsArrayList?.get(
                selectedTabPosition
            )?.cards?.size!! > 0
        ) {
            _binding?.recyclerViewFav?.visibility = View.VISIBLE
            _binding?.layoutNoData?.visibility = View.GONE

            setDataForItem(tabsArrayList?.get(selectedTabPosition)?.cards!!)

        } else {
            _binding?.layoutNoData?.visibility = View.VISIBLE
            _binding?.recyclerViewFav?.visibility = View.GONE
            _binding?.textViewNoData?.text = noDataText
        }
    }

    private fun setDataForItem(cardList: List<Card>) {
        var geoStoreAdapter = GeoStoreAdapter(requireContext(), cardList)
        val mLayoutManager: RecyclerView.LayoutManager = GridLayoutManager(requireContext(), 2)
        _binding?.recyclerViewFav?.setLayoutManager(mLayoutManager)
        _binding?.recyclerViewFav?.adapter = geoStoreAdapter
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
                    if (lat.equals("") && lng.equals(""))
                    {
                        Toast.makeText(requireContext(),"Please wait we are getting your current location.",Toast.LENGTH_SHORT).show()
                    }
                    getLastLocation()
                }
                else{
                    checkPermissionWithRequestPermission()
                }

            }
        })

        _binding?.recyclerViewFav?.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (isLoadMoreAvailable!! && tabsArrayList?.get(selectedTabPosition)?.cards?.size!! < tabsArrayList?.get(
                            selectedTabPosition
                        )?.tab_count!!
                    ) {
                        val url = loadMoreUrl.split(ApiClient.BASE_SERVER_URL)
                        if(isSearchAvailable) {
                            favTabViewModel.searchLoadMoreApiData(url.get(1))
                        } else {
                            favTabViewModel.getFavouriteTabLoadMoreApiData(url.get(1))
                        }
                        //setObserverForLoadMoreData()
                    }
                }
            }
        })
    }

    private fun stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask?.cancel()
            timer?.cancel()
            timer = null
            timerTask = null
            try {
                position = layoutManager!!.findFirstCompletelyVisibleItemPosition()
            } catch (e: Exception) {
                e.printStackTrace()
            }
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

    override fun onResume() {
        super.onResume()
        runAutoScrollBanner()
    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollBanner()
    }

    override fun onStart() {
        dialog?.dismiss()
        super.onStart()
        if (!isSearchAvailable)
            setLocation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GeoStoreFragment.REQUEST_CODE && resultCode == GeoStoreFragment.RESULT_CODE) {
            selectedFilters = ""
            val testResult = data?.getIntegerArrayListExtra(GeoStoreFragment.EXTRA_KEY_TEST)
            setObserverForLoadMoreData()
            checkStatusFromFilter = true
            if (testResult != null && testResult.size > 0) {
                selectedFilterIds = testResult
                selectedFilters = convertIntArrayToCommaSeparated(testResult)
            } else {
                selectedFilterIds.clear()
            }
            if(isSearchAvailable) {
                favTabViewModel.searchApiData(
                    "",
                    searchKeyword,
                    selectedFilters,
                    selectedSorting,
                    lat,
                    lng,
                    App.mLocalStore?.accessToken
                )
            } else {
                favTabViewModel.getFavoriteTabApiData(
                    selectedSorting,
                    selectedFilters,
                    lat, lng,
                    App.mLocalStore?.accessToken
                )
            }
        }
    }

    fun convertIntArrayToCommaSeparated(array: ArrayList<Int>): String {
        var filter = ""
        for (i in array.indices) {
            if (i == array.size - 1) {
                filter = filter + array.get(i).toString()
            } else {
                filter = filter + array.get(i).toString() + ","
            }
        }
        return filter
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
            favTabViewModel.getFavoriteTabApiData(
                selectedSorting,
                selectedFilters,
                lat, lng,
                App.mLocalStore?.accessToken)

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
            favTabViewModel.getFavoriteTabApiData(
                selectedSorting,
                selectedFilters,
                lat, lng,
                App.mLocalStore?.accessToken)
        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.show()

    }

    /*
    * method to check all condition for location capturing
    * */
    fun setLocation() {

        if (checkStatusFromFilter == false) {
            if (CheckPermission()) {
                if (!CheckPermission()) {
                    RequestPermission()
                } else if (!isLocationEnabled()) {
                    showConfirmationDialog(requireContext())
                } else if (isLocationEnabled() == true && CheckPermission() == true) {
                    getLastLocation()
                }
            } else {
                favTabViewModel.getFavoriteTabApiData(
                    selectedSorting,
                    selectedFilters,
                    lat, lng,
                    App.mLocalStore?.accessToken
                )
            }
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

    fun checkPermissionWithRequestPermission()
    {
        if (!CheckPermission())
        {
            RequestPermission()
        }
        else if (CheckPermission() && !isLocationEnabled())
        {
            showConfirmationDialog(requireContext())
        }
    }

    /*
    * Used for Search
    * */
    fun getSearch(keyword: String) {
        searchKeyword = keyword
        geoStoreCategoryAdapter = null
        tabsArrayList = null
        favTabViewModel.searchApiData(
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
        geoStoreCategoryAdapter = null
        tabsArrayList = null
        favTabViewModel.searchApiData(
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
        geoStoreCategoryAdapter = null
        if(isSearchAvailable) {
            tabsArrayList = null
        }
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
        isSearchAvailable = false
        geoStoreCategoryAdapter = null
        tabsArrayList = null
        setObserverForLoadMoreData()
        favTabViewModel.getFavoriteTabApiData(
            selectedSorting,
            selectedFilters,
            lat, lng,
            App.mLocalStore?.accessToken
        )
    }

    fun searchObserver() {
        favTabViewModel.searchLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is ResponseListener.Success -> {
                    isSearchAvailable = true
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

}