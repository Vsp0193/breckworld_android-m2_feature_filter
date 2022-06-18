package com.breckworld.ui.main.home.preview_adapter

import androidx.databinding.ObservableField
import com.breckworld.architecture.BaseItemState
import com.breckworld.repository.local.model.preview.PreviewModel
import com.breckworld.repository.local.model.preview.VideoPreviewModel

class HomePreviewItemState(
    private val item: PreviewModel,
    private val listener: HomePreviewRvAdapter.Listener<PreviewModel>
) : BaseItemState() {
    val previewImage = ObservableField<Int>()
    val previewTitle = ObservableField<String>()

    init {
        when (item) {
            is VideoPreviewModel -> {
                item.imagePreview?.let {
                    previewImage.set(it)
                }
                previewTitle.set(item.imgName)
            }
        }
    }

    fun onItemClick() {
        listener.onItemClick(item)
    }
}