package com.breckworld.ui.main.search

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseItemState
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.repository.local.model.CityModel

/**
 * @author Dmytro Bondarenko
 *         Date: 04.06.2019
 *         Time: 16:20
 *         E-mail: bondes87@gmail.com
 */
class CityItemState(
    private val item: CityModel,
    private val listener: BaseRecyclerViewAdapter.Listener<CityModel>
) : BaseItemState() {

    var title = MutableLiveData<String>()
    var photoResId = MutableLiveData<Int>()

    init {
        title.value = item.title
        photoResId.value = item.imageResId
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}