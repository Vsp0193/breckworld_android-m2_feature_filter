package com.breckworld.ui.main.searchResults

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.remote.http.model.SearchResult
import com.breckworld.databinding.ItemSearchResultBinding

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:07
 *         E-mail: bondes87@gmail.com
 */
class SearchResultsRecyclerAdapter (list: MutableList<SearchResult>,
                                     listener: BaseRecyclerViewAdapter.Listener<SearchResult>)
    : BaseRecyclerViewAdapter<SearchResultItemState, ItemSearchResultBinding, SearchResult>(list, listener) {

    override fun itemState(item: SearchResult, listener: BaseRecyclerViewAdapter.Listener<SearchResult>): SearchResultItemState = SearchResultItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_search_result
}