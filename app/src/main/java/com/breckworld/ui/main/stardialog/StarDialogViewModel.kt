package com.breckworld.ui.main.stardialog

import androidx.lifecycle.MutableLiveData
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.local.model.preview.VideoPreviewModel
import com.breckworld.repository.remote.http.model.SearchResult
import java.io.Serializable

class StarDialogViewModel : BaseMVVMViewModel<StarDialogFragment.Events>() {

    var title = "Castle Acre Ruins"
    var videoId = ""
    var imageUrl = ""
    var description = "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
    var fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
    var badges = 0
    var url = ""

    fun initData(any: Serializable?) {
        when (any) {
            null -> {
                title = "Castle Acre Ruins"
                videoId = ""
                imageUrl = ""
                description =
                    "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
                fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
            }
            is SearchResult -> {
                any.title?.let { title= it }
                any.videoId?.let {
                    videoId = if (it.isNotBlank()) {
                        it
                    } else {
                        ""
                    }
                }
                any.image?.let { imageUrl = it }
                any.description?.let { description = it }
                any.fullAddress?.let { fullAddress = it }
                any.website?.let { url = it }
            }
            is LocationStarDB -> {
                title = any.title
                videoId = if (any.videoId.isNotBlank()) any.videoId else ""
                imageUrl = any.image
                description = any.description
                fullAddress = any.fullAddress
                url = any.website
            }
            is VideoPreviewModel -> {
                title = any.title
                videoId = any.video_id.toString()
                imageUrl = any.imageURL
                description = any.description
                fullAddress = any.adress
                badges = any.badgeNumber
                url = any.url
            }
            else -> {
                title = "Castle Acre Ruins"
                videoId = ""
                imageUrl = ""
                description =
                    "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
                fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
            }
        }
    }

    fun onBack() {
        _eventsLiveData.value = Event(StarDialogFragment.Events.BACK)
    }

    fun closeDialog() {
        onBack()
    }
}