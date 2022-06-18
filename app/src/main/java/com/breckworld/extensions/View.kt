package com.breckworld.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.EditText
import com.breckworld.R

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.animateGone(duration: Long = 100) {
    animate().setDuration(duration)
            .alpha(0f)
            .withEndAction {
                visibility = View.GONE
            }
}

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.animateVisible(duration: Long = 100) {
    alpha = 0f
    visibility = View.VISIBLE
    animate().setDuration(duration)
            .alpha(1f)
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.animateInvisible(duration: Long = 100) {
    animate().setDuration(duration)
            .alpha(0f)
            .withEndAction {
                visibility = View.INVISIBLE
            }
}

fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}

fun View.slideUp(duration: Int = 200, dpMoveUp: Int = 56, start: Boolean = true): Animation {
    val animate = TranslateAnimation(0f, 0f, dpMoveUp.toPx().toFloat(), 0f)
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationStart(p0: Animation?) {
            visibility = View.VISIBLE
        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    })
    animate.duration = duration.toLong()
    animate.fillAfter = true
    if (start) this.startAnimation(animate)
    return animate
}


fun View.slideDown(duration: Int = 200, dpMoveDown: Int = 100,
                   hideViewAfter: Boolean = false,
                   start: Boolean = true): Animation {
    val animate = TranslateAnimation(0f, 0f, 0f, height.toFloat())
    animate.duration = duration.toLong()
    animate.fillAfter = true
    animate.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(p0: Animation?) {
            if (hideViewAfter) gone() else visible()
        }

        override fun onAnimationStart(p0: Animation?) {

        }

        override fun onAnimationRepeat(p0: Animation?) {

        }

    })
    if (start) this.startAnimation(animate)
    return animate
}

fun View.startSlideUpAnimation(duration: Long = 200) {
    val slide_up = AnimationUtils.loadAnimation(context,
            R.anim.anim_slide_down_up_in)
    slide_up.duration = duration
    visibility = View.VISIBLE
    slide_up.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
        }

        override fun onAnimationStart(p0: Animation?) {
            visibility = View.VISIBLE
        }

    })
    startAnimation(slide_up)
}

fun View.startSlideDownAnimation(duration: Long = 200) {
    val slide_up = AnimationUtils.loadAnimation(context,
            R.anim.anim_slide_up_down_out)
    slide_up.duration = duration
    visibility = View.VISIBLE
    slide_up.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(p0: Animation?) {
        }

        override fun onAnimationEnd(p0: Animation?) {
            visibility = View.GONE
        }

        override fun onAnimationStart(p0: Animation?) {
            visibility = View.VISIBLE
        }

    })
    startAnimation(slide_up)
}

fun View.resizeView(newWidth: Int? = null, newHeight: Int? = null) {
    try {
        val params = layoutParams
        params.height = newHeight ?: height
        requestLayout()
    } catch (e: Exception) {
        e.printStackTrace()
    }

}

/* Edit Text */
fun EditText.text(): String = text.toString()
