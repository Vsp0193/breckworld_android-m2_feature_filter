package com.breckworld.view

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.widget.FrameLayout
import org.jetbrains.annotations.NotNull
import androidx.core.view.ViewCompat.onApplyWindowInsets
import android.view.WindowInsets
import android.widget.RelativeLayout
import com.breckworld.extensions.toPx
import com.breckworld.utils.Utils


class CustomInsetFrameLayout : FrameLayout {

    private val mInsets = IntArray(4)

    var statusBarHeight: Int = 24f.toPx()

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
    }

    override fun onApplyWindowInsets(insets: WindowInsets): WindowInsets {
        statusBarHeight = insets.systemWindowInsetTop
        mInsets[0] = insets.systemWindowInsetLeft
        mInsets[1] = insets.systemWindowInsetTop
        mInsets[2] = insets.systemWindowInsetRight
        return super.onApplyWindowInsets(
            insets.replaceSystemWindowInsets(
                0, 0, 0,
                insets.systemWindowInsetBottom
            )
        )
    }
}