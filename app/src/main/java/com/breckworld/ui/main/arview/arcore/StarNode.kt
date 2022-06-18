package com.breckworld.ui.main.arview.arcore

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.breckworld.R
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.local.model.MarkerModel
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.collision.Box
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.DpToMetersViewSizer
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ViewRenderable

private const val DP_PER_METERS = 85 //125 // default 250

@RequiresApi(Build.VERSION_CODES.N)
class StarNode(private val context: Context?,
               private val markerModel: LocationStarDB,
               private val starRenderable: ModelRenderable?,
               private val starClickListener: ((LocationStarDB) -> Unit)? = null,
               private val deactivateListener: ((LocationStarDB) -> Unit)? = null): Node() {

    private var infoCard: InfoCardNode? = null
    private var starVisual: RotatingNode? = null

    override fun onActivate() {
        if (scene == null) throw IllegalStateException("Scene is null!")

        if (starVisual == null) {
            starVisual = RotatingNode()
            starVisual!!.setParent(this)
            starVisual!!.renderable = starRenderable
            starVisual!!.setOnTapListener { hitTestResult, motionEvent ->
                starClickListener?.invoke(markerModel)
            }

            ViewRenderable.builder()
                .setView(context, R.layout.info_card_view)
                .setSizer(DpToMetersViewSizer(DP_PER_METERS))
                .build()
                .thenAccept { renderable ->
                    infoCard?.renderable = renderable
                    val infocardLayout = renderable.view
                    val textView: TextView = infocardLayout.findViewById(R.id.tv_info_text)
                    textView.text = markerModel.title
                }
                .exceptionally { throwable ->
                    throw AssertionError("Could not load info card", throwable)
                }
        }

        if (infoCard == null) {
            val starBox = starVisual!!.collisionShape as Box
            val starSize = starBox.size.y

            infoCard = InfoCardNode()
            infoCard?.setParent(starVisual)
            infoCard?.localPosition = Vector3(0.0f, starSize, 0.0f)
        }

    }

    override fun onDeactivate() {
        markerModel.activated = false
        deactivateListener?.invoke(markerModel)
        super.onDeactivate()
    }
}