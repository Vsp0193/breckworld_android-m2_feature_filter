package com.breckworld.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

open class HorizontalSpaceMarginItemDecoration(
    private val marginStart: Int,
    private val marginEnd: Int,
    private val marginInternal: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildLayoutPosition(view)
        parent.childCount
        val adapter = parent.adapter
        if (adapter != null && position == adapter.itemCount - 1) {
            outRect.right = marginEnd
        }
        if (position == 0) outRect.left = marginStart
        else outRect.left = marginInternal
    }
}