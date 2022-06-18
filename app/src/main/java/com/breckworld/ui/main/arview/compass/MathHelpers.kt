package com.breckworld.ui.main.arview.compass

import com.google.ar.core.Pose
import com.google.ar.sceneform.math.Vector3

import java.util.Arrays
import kotlin.math.abs
import kotlin.math.min

/**
 * Created by inio on 2/28/18.
 */

object MathHelpers {

    /**
     * Returns a pose rotating about the origin so that the point `from` is rotated to be
     * colinear with the origin and `to`.  Rotation takes the shortest path.
     */
    fun rotateBetween(fromRaw: FloatArray, toRaw: FloatArray): Pose {
        val from = Arrays.copyOf(fromRaw, 3)
        normalize(from)
        val to = Arrays.copyOf(toRaw, 3)
        normalize(to)

        val cross = FloatArray(3)
        cross[0] = from[1] * to[2] - from[2] * to[1]
        cross[1] = from[2] * to[0] - from[0] * to[2]
        cross[2] = from[0] * to[1] - from[1] * to[0]
        val dot = from[0] * to[0] + from[1] * to[1] + from[2] * to[2]
        val angle = Math.atan2(norm(cross).toDouble(), dot.toDouble()).toFloat()
        normalize(cross)

        val sinhalf = Math.sin((angle / 2.0f).toDouble()).toFloat()
        val coshalf = Math.cos((angle / 2.0f).toDouble()).toFloat()

        return Pose.makeRotation(cross[0] * sinhalf, cross[1] * sinhalf, cross[2] * sinhalf, coshalf)
    }

    fun axisRotation(axis: Int, angleRad: Float): Pose {
        val sinHalf = Math.sin((angleRad / 2).toDouble()).toFloat()
        val cosHalf = Math.cos((angleRad / 2).toDouble()).toFloat()

        when (axis) {
            0 -> return Pose.makeRotation(sinHalf, 0f, 0f, cosHalf)
            1 -> return Pose.makeRotation(0f, sinHalf, 0f, cosHalf)
            2 -> return Pose.makeRotation(0f, 0f, sinHalf, cosHalf)
            else -> throw IllegalArgumentException("invalid axis $axis")
        }
    }

    /**
     * Returns the 2-norm of the input array.
     */
    fun norm(`in`: FloatArray): Float {
        var sum = 0f
        for (f in `in`) {
            sum += f * f
        }
        return Math.sqrt(sum.toDouble()).toFloat()
    }

    /**
     * Normalizes the input array in-place.
     */
    fun normalize(`in`: FloatArray) {
        val scale = 1 / norm(`in`)
        for (i in `in`.indices) {
            `in`[i] *= scale
        }
    }

    fun bearingDiff(a: Float, b: Float): Float {
        val a1 = a + 180f
        val a2 = b + 180f
        val res = min(
            if (a1 - a2 < 0) a1 - a2 + 360f else a1 - a2,
            if (a2 - a1 < 0) a2 - a1 + 360f else a2 - a1
        )
        return res - 180f
        //return -(abs(a - b + 180f) % 360f - 180f)
    }

    fun getDistanceBetweenVectorsInMeters(to: Vector3, from: Vector3): Float {
        // Compute the difference vector between the two hit locations.
        val dx = to.x - from.x
        val dy = to.y - from.y
        val dz = to.z - from.z

        // Compute the straight-line distance (distanceMeters)
        return Math.sqrt((dx * dx + dy * dy + dz * dz).toDouble()).toFloat()
    }
}
