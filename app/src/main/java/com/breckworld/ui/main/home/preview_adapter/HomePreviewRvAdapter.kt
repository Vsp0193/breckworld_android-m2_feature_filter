package com.breckworld.ui.main.home.preview_adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.breckworld.R
import com.breckworld.architecture.BaseRecyclerViewAdapter
import com.breckworld.databinding.ItemHomePreviewBinding
import com.breckworld.databinding.ItemHomePreviewSearchBinding
import com.breckworld.repository.local.model.preview.PreviewModel
import com.breckworld.repository.local.model.preview.SearchPreviewModel
import com.breckworld.repository.local.model.preview.VideoPreviewModel

class HomePreviewRvAdapter(list: MutableList<PreviewModel>, listener: Listener<PreviewModel>)
    : BaseRecyclerViewAdapter<HomePreviewItemState, ViewDataBinding, PreviewModel>(list, listener) {

    override fun layoutResId(): Int = R.layout.item_home_preview
    protected fun secondLayoutResId(): Int = R.layout.item_home_preview_search

    override fun getItemViewType(position: Int): Int =
        when(list?.get(position)) {
            is VideoPreviewModel -> 0
            is SearchPreviewModel -> 1
            null -> 1
            else -> throw RuntimeException("Invalid type")
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewDataBinding> {
        val inflater = LayoutInflater.from(parent.context)
        when (viewType) {
            1 -> {
                val itemBinding: ItemHomePreviewSearchBinding = DataBindingUtil.inflate(inflater, secondLayoutResId(), parent, false)
                return BaseViewHolder(itemBinding)
            }
            0 -> {
                val itemBinding: ItemHomePreviewBinding = DataBindingUtil.inflate(inflater, layoutResId(), parent, false)
                return BaseViewHolder(itemBinding)
            }
            else -> throw RuntimeException("Invalid type")
        }
    }

    override fun itemState(item: PreviewModel, listener: BaseRecyclerViewAdapter.Listener<PreviewModel>): HomePreviewItemState =
        HomePreviewItemState(item, listener as Listener<PreviewModel>)

    interface Listener<I> : BaseRecyclerViewAdapter.Listener<I>

}