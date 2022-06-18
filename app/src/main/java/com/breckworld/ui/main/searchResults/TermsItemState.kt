package com.breckworld.ui.main.searchResults

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:08
 *         E-mail: bondes87@gmail.com
 */
class TermsItemState(
    private val item: String,
    private val listener: BaseRecyclerViewAdapter.Listener<String>
) : BaseItemState() {

    var term = MutableLiveData<String>()

    init {
        term.value = item
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}