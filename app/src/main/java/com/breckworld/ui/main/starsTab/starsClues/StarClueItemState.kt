package com.breckworld.ui.main.starsTab.starsClues

import androidx.lifecycle.MutableLiveData
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.database.model.LocationStarDB

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 22:42
 *         E-mail: bondes87@gmail.com
 */
class StarClueItemState(
    private val item: LocationStarDB,
    private val listener: BaseRecyclerViewAdapter.Listener<LocationStarDB>
) : BaseItemState() {

    var title = MutableLiveData<String>()
    var description = MutableLiveData<String>()
    var photoUrl = MutableLiveData<String>()
    var placeholderId = MutableLiveData<Int>()
    var radius = MutableLiveData<Int>()

    init {
        title.value = item.title
        description.value = item.description
        photoUrl.value = item.image
        placeholderId.value = R.drawable.ic_placeholder_rounded
        radius.value = App.getResDimensionPixelSize(R.dimen.photo_corner)
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}