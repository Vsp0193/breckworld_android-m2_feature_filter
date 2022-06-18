package com.breckworld.ui.main.arview.arcore

import android.animation.ObjectAnimator
import android.view.animation.LinearInterpolator
import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.QuaternionEvaluator
import com.google.ar.sceneform.math.Vector3

class RotatingNode : Node() {
    // We'll use Property Animation to make this node rotate.
    private var orbitAnimation: ObjectAnimator? = null
    private var degreesPerSecond = 180.0f

    private val animationDuration: Long
        get() = (1000 * 360 / degreesPerSecond).toLong()

    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        // Animation hasn't been set up.
        if (orbitAnimation == null) {
            return
        }

        orbitAnimation!!.resume()
        val animatedFraction = orbitAnimation!!.animatedFraction
        orbitAnimation!!.duration = animationDuration
        orbitAnimation!!.setCurrentFraction(animatedFraction)
    }

    fun setDegreesPerSecond(degreesPerSecond: Float) {
        this.degreesPerSecond = degreesPerSecond
    }

    override fun onActivate() {
        startAnimation()
    }

    override fun onDeactivate() {
        stopAnimation()
    }

    private fun startAnimation() {
        if (orbitAnimation != null) {
            return
        }

        orbitAnimation = createAnimator()
        orbitAnimation!!.target = this
        orbitAnimation!!.duration = animationDuration
        orbitAnimation!!.start()
    }

    private fun stopAnimation() {
        if (orbitAnimation == null) {
            return
        }
        orbitAnimation!!.cancel()
        orbitAnimation = null
    }

    private fun createAnimator(): ObjectAnimator {
        // Node's setLocalRotation method accepts Quaternions as parameters.
        // First, set up orientations that will animate a circle.
        val orientations = arrayOfNulls<Quaternion>(4)
        // Rotation to apply first, to tilt its axis.
        val baseOrientation = Quaternion.axisAngle(Vector3(1.0f, 0f, 0.0f), 0f)
        for (i in orientations.indices) {
            var angle = (i * 360 / (orientations.size - 1)).toFloat()
            angle = 360 - angle
            val orientation = Quaternion.axisAngle(Vector3(0.0f, 1.0f, 0.0f), angle)
            orientations[i] = Quaternion.multiply(baseOrientation, orientation)
        }

        val orbitAnimation = ObjectAnimator()
        // Cast to Object[] to make sure the varargs overload is called.
        orbitAnimation.setObjectValues(*orientations)

        // Next, give it the localRotation property.
        //orbitAnimation.propertyName = "localRotation"
        orbitAnimation.setPropertyName("localRotation")
        // Use Sceneform's QuaternionEvaluator.
        orbitAnimation.setEvaluator(QuaternionEvaluator())

        //  Allow orbitAnimation to repeat forever
        orbitAnimation.repeatCount = ObjectAnimator.INFINITE
        orbitAnimation.repeatMode = ObjectAnimator.RESTART
        orbitAnimation.interpolator = LinearInterpolator()
        orbitAnimation.setAutoCancel(true)

        return orbitAnimation
    }
}

