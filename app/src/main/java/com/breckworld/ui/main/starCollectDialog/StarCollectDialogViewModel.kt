package com.breckworld.ui.main.starCollectDialog

import android.location.Location
import com.breckworld.App
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.database.model.StarDB
import java.io.Serializable
import java.lang.NumberFormatException

class StarCollectDialogViewModel : BaseMVVMViewModel<StarCollectDialogFragment.Events>() {

    var title = ""
    var collectText = "Castle Acre Ruins"
    var videoId = ""
    var imageUrl = ""
    var url = ""

    val collectedStarsCount = repository.getCollectedStarsCount()

    var location: Location? = null

    fun initData(any: Serializable?) {
        when (any) {
            is StarDB -> {
                collectText = App.getQuantityString(R.plurals.collected_star, collectedStarsCount.value ?: 0, collectedStarsCount.value ?: 0, any.title)
                try {
                    videoId = any.videoId
                } catch (e: NumberFormatException) {
                    videoId = ""
                }
                imageUrl = any.image
                title = any.title
            }
            is LocationStarDB -> {
                collectText = App.getQuantityString(R.plurals.collected_star, collectedStarsCount.value ?: 0, collectedStarsCount.value ?: 0, any.title)
                try {
                    videoId = any.videoId
                } catch (e: NumberFormatException) {
                    videoId = ""
                }
                imageUrl = any.image
                title = any.title
            }
            null -> {
                collectText = "Castle Acre Ruins"
                title = ""
                videoId = ""
            }

            else -> {
                collectText = "Castle Acre Ruins"
                title = ""
                videoId = ""
            }
        }
    }

    fun onBack() {
        _eventsLiveData.value = Event(StarCollectDialogFragment.Events.BACK)
    }

    fun closeDialog() {
        onBack()
    }
}