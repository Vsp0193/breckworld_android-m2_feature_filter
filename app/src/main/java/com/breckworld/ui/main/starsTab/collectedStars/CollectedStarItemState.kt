package com.breckworld.ui.main.starsTab.collectedStars

import androidx.lifecycle.MutableLiveData
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.database.model.StarDB
import com.breckworld.utils.DateUtils

/**
 * @author Dmytro Bondarenko
 *         Date: 30.05.2019
 *         Time: 22:42
 *         E-mail: bondes87@gmail.com
 */
class CollectedStarItemState(
    private val item: StarDB,
    private val listener: BaseRecyclerViewAdapter.Listener<StarDB>
) : BaseItemState() {

    var title = MutableLiveData<String>()
    var date = MutableLiveData<String>()
    var photoUrl = MutableLiveData<String>()
    var placeholderId = MutableLiveData<Int>()
    var radius = MutableLiveData<Int>()

    init {
        title.value = item.title
        date.value = getDate(item.dateAdded)
        photoUrl.value = item.image
        placeholderId.value = R.drawable.ic_placeholder_rounded
        radius.value = App.getResDimensionPixelSize(R.dimen.photo_corner)
    }

    private fun getDate(it: String): String {
        return DateUtils.changeDateFormat(
            DateUtils.getCalendar(it, DateUtils.PATTERN_DATE_1),
            DateUtils.PATTERN_DATE_2
        )
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}