package com.breckworld.ui.main.searchResults

import androidx.lifecycle.MutableLiveData
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.remote.http.model.SearchResult

/**
 * @author Dmytro Bondarenko
 *         Date: 05.06.2019
 *         Time: 17:08
 *         E-mail: bondes87@gmail.com
 */
class SearchResultItemState(
    private val item: SearchResult,
    private val listener: BaseRecyclerViewAdapter.Listener<SearchResult>
) : BaseItemState() {

    var title = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var photoUrl = MutableLiveData<String>()
    var placeholderId = MutableLiveData<Int>()
    var radius = MutableLiveData<Int>()

    init {
        item.title?.let { title.value = it }
        item.description?.let { description.value = it }
        item.image?.let { photoUrl.value = it }
        placeholderId.value = R.drawable.ic_placeholder_rounded
        radius.value = App.getResDimensionPixelSize(R.dimen.photo_corner)
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}