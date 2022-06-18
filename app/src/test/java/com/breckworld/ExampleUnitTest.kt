package com.breckworld

import com.breckworld.ui.main.arview.compass.MathHelpers
import org.junit.Test

import org.junit.Assert.*
import java.lang.Math.abs

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testFloat() {
        val bearing: Float = 129.25986f
        val location: Float = -52.29394f
        val alpha: Float = -(abs(bearing - location + 180f) % 360f - 180f)
        val delta: Float = (bearing - location + 180f)
        val ostatok: Float = abs(delta % 360f)
        val result: Float = -(ostatok - 180f)
        println(alpha)
        println(delta)
        println(ostatok)
        println(result)
    }
}
