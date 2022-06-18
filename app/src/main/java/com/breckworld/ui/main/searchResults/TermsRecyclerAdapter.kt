package com.breckworld.ui.main.searchResults

import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.remote.http.model.SearchResult
import com.breckworld.databinding.ItemTermBinding

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:07
 *         E-mail: bondes87@gmail.com
 */
class TermsRecyclerAdapter (list: MutableList<String>,
                            listener: BaseRecyclerViewAdapter.Listener<String>)
    : BaseRecyclerViewAdapter<TermsItemState, ItemTermBinding, String>(list, listener) {

    override fun itemState(item: String, listener: BaseRecyclerViewAdapter.Listener<String>): TermsItemState = TermsItemState(item, listener)

    override fun layoutResId(): Int = R.layout.item_term
}