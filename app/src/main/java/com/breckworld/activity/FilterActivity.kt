package com.breckworld.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.breckworld.R
import com.breckworld.adapter.FilterAdapter
import com.breckworld.databinding.ActivityFilterBinding
import com.breckworld.fragment.GeoStoreFragment
import com.breckworld.model.geostore.Filter


class FilterActivity : AppCompatActivity() {

    private var filterList: ArrayList<Filter>? = null
    private lateinit var filterBinding: ActivityFilterBinding
    private var selectedFilterIds: MutableList<Int> = ArrayList()
    private var isFirstTimeCall: Boolean?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterBinding = ActivityFilterBinding.inflate(layoutInflater)
        setContentView(filterBinding.root)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.header_color)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.header_color)

        if (intent != null) {
            filterList = intent.getParcelableArrayListExtra("filters")
            isFirstTimeCall = intent.getBooleanExtra("isFirstTimeCall", true)
            selectedFilterIds = intent.getIntegerArrayListExtra("selectedIds")!!
        }

        filterBinding.textViewClose.setOnClickListener {
            finish()
        }

        filterBinding.buttonClearFilter.setOnClickListener {
            selectedFilterIds.clear()
            setDataToAdapterForFilter(true)
        }

        filterBinding.buttonSearch.setOnClickListener {
            val intent = Intent()
            intent.putIntegerArrayListExtra(
                GeoStoreFragment.EXTRA_KEY_TEST,
                selectedFilterIds as java.util.ArrayList<Int>
            )
            setResult(
                GeoStoreFragment.RESULT_CODE,
                intent
            )
            finish()
        }

        var isDataAvailable = false
        for(i in filterList?.indices!!) {
            if (filterList?.get(i)?.items?.size!! > 0) {
                isDataAvailable = true
                break
            } else {
                isDataAvailable = false
            }
        }

        if (isDataAvailable) {
            filterBinding.layoutMainContent.visibility = View.VISIBLE
            filterBinding.layoutNoData.visibility = View.GONE
            setDataToAdapterForFilter(false)
        } else {
            filterBinding.layoutMainContent.visibility = View.GONE
            filterBinding.layoutNoData.visibility = View.VISIBLE
        }

    }

    fun setDataToAdapterForFilter(isClearFilter: Boolean) {
        val filterAdapter = FilterAdapter(this, filterList!!, isClearFilter, selectedFilterIds)
        filterBinding.recyclerViewFilter.layoutManager = LinearLayoutManager(this)
        filterBinding.recyclerViewFilter.adapter = filterAdapter
        filterAdapter.setOnClickListener(object :
            FilterAdapter.OnClickListener {
            override fun onFilterClickListener(
                position: Int,
                needToAdd: Boolean,
                outerPosition: Int
            ) {

                if (needToAdd) {
                    if (!selectedFilterIds.contains(
                            filterList?.get(outerPosition)?.items?.get(
                                position
                            )?.id!!
                        )
                    )
                        selectedFilterIds.add(filterList?.get(outerPosition)?.items?.get(position)?.id!!)
                } else {
                    if (selectedFilterIds.contains(
                            filterList?.get(outerPosition)?.items?.get(
                                position
                            )?.id!!
                        )
                    )
                        selectedFilterIds.remove(filterList?.get(outerPosition)?.items?.get(position)?.id!!)
                }
            }

        })
    }
}