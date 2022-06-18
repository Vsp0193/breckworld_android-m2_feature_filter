package com.breckworld.ui.main.arview.arcore

import android.view.View
import android.view.WindowManager
import com.google.ar.sceneform.ux.ArFragment

class NFArFragment : ArFragment() {
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        val activity = activity
        if (hasFocus && activity != null) {
            activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            activity.window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN
            )
        }
    }
}
