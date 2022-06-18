package com.breckworld.ui.main.searchResults

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.FragmentSearchResultsBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.livedata.EventObserver
import com.breckworld.repository.local.model.SearchModel
import com.breckworld.repository.remote.http.model.SearchResult
import kotlinx.android.synthetic.main.fragment_search_results.*

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:06
 *         E-mail: bondes87@gmail.com
 */
class SearchResultsFragment :
    BaseMVVMFragment<SearchResultsViewModel, FragmentSearchResultsBinding, SearchResultsFragment.Events>() {

    override fun viewModelClass(): Class<SearchResultsViewModel> = SearchResultsViewModel::class.java

    override fun layoutResId(): Int = R.layout.fragment_search_results

    private var termsRecyclerAdapter: TermsRecyclerAdapter? = null
    private var searchResultsRecyclerAdapter: SearchResultsRecyclerAdapter? = null
    private var searchResultsLayoutManager: LinearLayoutManager? = null

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver { events ->
            when (events) {
                Events.RESULT_LOADED -> resultsLoaded()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            (getSerializable(Constants.KEY_SEARCH_MODEL) as SearchModel?)?.let {
                viewModel.searchModel = it
                viewModel.terms = it.term?.split(Constants.PLUS)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showBottomNavigation(false)
        setupStatusbar(true, true)
        setupStatusBarPadding()
        if (searchResultsRecyclerAdapter == null) {
            viewModel.search()
        }
        initViews()
    }

    private fun setupStatusBarPadding() {
        val top = constraint_layout_content.paddingTop + getStatusBarHeight()
        val bottom = constraint_layout_content.paddingBottom
        val start = constraint_layout_content.paddingStart
        val end = constraint_layout_content.paddingEnd
        constraint_layout_content.setPadding(start, top, end, bottom)
    }

    private fun resultsLoaded() {
        if (searchResultsRecyclerAdapter?.itemCount == 0) {
            searchResultsRecyclerAdapter?.replace(viewModel.results)
        } else {
            searchResultsRecyclerAdapter?.addAll(viewModel.results)
        }
    }

    private var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val defaultOffset = 16f
            var delta = 0f
            val position = searchResultsLayoutManager?.findFirstVisibleItemPosition()
            if (position != null) {
                val view = searchResultsLayoutManager?.findViewByPosition(position)
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
        searchResultsLayoutManager = LinearLayoutManager(context)
        recycler_view_results?.layoutManager = searchResultsLayoutManager
        recycler_view_results.addOnScrollListener(scrollListener)
        initSearchResultsRecyclerAdapter()

        recycler_view_terms?.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        initTermsRecyclerAdapter()
    }

    private fun initSearchResultsRecyclerAdapter() {
        if (searchResultsRecyclerAdapter == null) {
            searchResultsRecyclerAdapter =
                SearchResultsRecyclerAdapter(viewModel.results,
                    object : BaseRecyclerViewAdapter.Listener<SearchResult> {
                        override fun onItemClick(item: SearchResult) {
                            val bundle = bundleOf(Constants.KEY_ANY to item)
                            findNavController().navigateTo(R.id.action_global_to_star_dialog, bundle)
                        }
                    })
        }
        recycler_view_results?.adapter = searchResultsRecyclerAdapter
    }

    private fun initTermsRecyclerAdapter() {
        if (termsRecyclerAdapter == null) {
            termsRecyclerAdapter =
                TermsRecyclerAdapter(viewModel.terms?.toMutableList()!!,
                    object : BaseRecyclerViewAdapter.Listener<String> {
                        override fun onItemClick(item: String) {
                        }
                    })
        }
        recycler_view_terms?.adapter = termsRecyclerAdapter
    }

    enum class Events {
        RESULT_LOADED
    }
}