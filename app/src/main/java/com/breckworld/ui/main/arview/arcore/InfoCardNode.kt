package com.breckworld.ui.main.arview.arcore

import com.google.ar.sceneform.FrameTime
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3

class InfoCardNode: Node() {
    override fun onUpdate(frameTime: FrameTime?) {
        super.onUpdate(frameTime)

        scene?.let {
            val cameraPosition = it.camera.worldPosition
            val cardPosition = worldPosition
            val direction = Vector3.subtract(cameraPosition, cardPosition)
            val lookRotation = Quaternion.lookRotation(direction, Vector3.up())
            worldRotation = lookRotation
        }
    }
}