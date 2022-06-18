package com.breckworld.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.breckworld.R
import com.breckworld.extensions.inflate

class ProgressLoadingOverlay @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0) : FrameLayout(context, attrs, style) {

    private var handleClick: Boolean = true

    init {
        val view = inflate(R.layout.widget_progress_loading_overlay, true)
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.ProgressLoadingOverlay, 0, 0)
            try {
                handleClick = a.getBoolean(R.styleable.ProgressLoadingOverlay_handle_click, true)
            } finally {
                a.recycle()
            }
        }
        view.isClickable = handleClick
        view.isFocusable = handleClick
        view.isFocusableInTouchMode = handleClick
    }
}