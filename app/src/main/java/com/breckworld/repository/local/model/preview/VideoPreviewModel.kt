package com.breckworld.repository.local.model.preview

import androidx.annotation.DrawableRes
import java.io.Serializable

data class VideoPreviewModel(
    val imgName: String,
    val url: String,
    val caption: String,
    val video_id: String,
    var imageURL: String,
    val title: String,
    val description: String,
    val adress: String,
    val latitude: Double,
    val longitude: Double,
    val badgeNumber: Int,
    @DrawableRes val imagePreview: Int? = null
): PreviewModel, Serializable