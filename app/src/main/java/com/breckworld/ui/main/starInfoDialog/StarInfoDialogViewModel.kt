package com.breckworld.ui.main.starInfoDialog

import androidx.lifecycle.MutableLiveData
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMViewModel
import com.breckworld.livedata.Event
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.database.model.StarDB
import com.breckworld.repository.local.model.preview.VideoPreviewModel
import java.io.Serializable

/**
 * @author Dmytro Bondarenko
 *         Date: 31.05.2019
 *         Time: 17:07
 *         E-mail: bondes87@gmail.com
 */
class StarInfoDialogViewModel : BaseMVVMViewModel<StarInfoDialogFragment.Events>() {

    var title = "Castle Acre Ruins"
    var videoId = ""
    var imageUrl = ""
    var description =
        "Castle Acre Ruins is a rare and complete example of a Norman settlement that includes a castle, village and church. It&apos;s free and there&apos;s a star at the summit"
    var fullAddress = "Pye&apos;s Lane, Castle Acre, PE32 2XB"
    val placeholderId = R.drawable.ic_placeholder
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
            is LocationStarDB -> {
                videoId = if (!any.videoId.isBlank()) {
                    any.videoId
                } else {
                    ""
                }
                title = any.title
                imageUrl = any.image
                description = any.description
                fullAddress = if (!any.fullAddress.isBlank()) {
                    any.fullAddress
                } else {
                   "No location"
                }
                badges = 0
            }
            is StarDB -> {
                videoId = if (!any.videoId.isBlank()) {
                    any.videoId
                } else {
                    ""
                }
                title = any.title
                imageUrl = any.image
                description = any.description
                fullAddress = if (!any.location.isBlank()) {
                    any.location
                } else {
                    "No location"
                }
                badges = 0
            }
            is VideoPreviewModel -> {
                videoId = ""
                title = any.title
                imageUrl = any.imageURL
                description = any.description
                fullAddress = if (!any.adress.isNullOrBlank()) {
                    any.adress
                } else {
                    "No location"
                }
                badges = any.badgeNumber
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
        _eventsLiveData.value = Event(StarInfoDialogFragment.Events.BACK)
    }

    fun closeDialog() {
        onBack()
    }
}