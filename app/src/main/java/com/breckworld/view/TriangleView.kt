package com.breckworld.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.breckworld.R

class TriangleView @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyleAttr: Int = 0): View(context, attrs, defStyleAttr) {

    @ColorRes
    private var mTriangleColor: Int = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.TriangleView,
            0, 0).apply {

            try {
                mTriangleColor = getResourceId(R.styleable.TriangleView_triangleColor, 0)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        if (mTriangleColor == 0) return

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.strokeWidth = 2f
        paint.color = ContextCompat.getColor(context, mTriangleColor)
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        val path = Path()
        path.moveTo(0f,0f)
        path.lineTo(width, 0f)
        //path.moveTo(width, 0f)
        path.lineTo(width / 2, height)
        //path.moveTo(width / 2, height)
        path.lineTo(0f,0f)
        path.close()

        canvas.drawPath(path, paint)
    }
}