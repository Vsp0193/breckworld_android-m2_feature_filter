package com.breckworld.ui.main.offerViewDialog

import androidx.lifecycle.MutableLiveData
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.OfferDB

class OfferViewViewModel : BaseMVVMViewModel<OfferViewFragment.Events>() {

    var title = ""
    var videoId = ""
    var description = ""
    var fullAddress = ""
    var placeholderId = R.drawable.ic_town
    var photoUrl = ""
    var isVideoExist = false

    fun initData(offerDB: OfferDB) {
        title = offerDB.title
        photoUrl = offerDB.image
        description = offerDB.description
        fullAddress = offerDB.location
        offerDB.videoId.let {
            videoId = if (it.isNotBlank()) {
                it
            } else {
                ""
            }
        }
        isVideoExist = videoId != ""
    }

    fun onBack() {
        _eventsLiveData.value = Event(OfferViewFragment.Events.BACK)
    }

    fun closeDialog() {
        onBack()
    }
}