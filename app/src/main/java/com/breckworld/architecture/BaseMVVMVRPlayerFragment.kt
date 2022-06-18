package com.breckworld.architecture

import androidx.databinding.ViewDataBinding
//import com.omnivirt.vrkit.Mode
//import com.omnivirt.vrkit.OnVRPlayerInteractionListener
//import com.omnivirt.vrkit.Quality
import java.lang.reflect.Array

abstract class BaseMVVMVRPlayerFragment<V : BaseMVVMViewModel<T>, B : ViewDataBinding, T : Enum<T>> :
    BaseMVVMFragment<V, B, T>()/*, OnVRPlayerInteractionListener */{

    /*
    override fun onVRPlayerCardboardChanged(p0: Mode?) {}

    override fun onVRPlayerEnded() {}

    override fun onVRPlayerCollapsed() {}

    override fun onVRPlayerDurationChanged(p0: Double?) {}

    override fun onVRPlayerExpanded() {}

    override fun onVRPlayerProgressChanged(p0: Double?) {}

    override fun onVRPlayerFragmentCreated() {}

    override fun onVRPlayerPaused() {}

    override fun onVRPlayerLongitudeChanged(p0: Double?) {}

    override fun onVRPlayerSkipped() {}

    override fun onVRPlayerStarted() {}

    override fun onVRPlayerLatitudeChanged(p0: Double?) {}

    override fun onVRPlayerSwitched(p0: String?, p1: Array?) {}

    override fun onVRPlayerLoaded(p0: Int?, p1: Quality?, p2: Mode?) {}

    override fun onVRPlayerVolumeChanged(p0: Double?) {}

    override fun onVRPlayerQualityChanged(p0: Quality?) {}

    override fun onVRPlayerBufferChanged(p0: Double?) {}

    override fun onVRPlayerSeekChanged(p0: Double?) {}
     */
}