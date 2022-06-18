package com.breckworld.ui.main.arview.arcore

import android.content.Context
import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.breckworld.R
import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.local.model.MarkerModel
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.DpToMetersViewSizer
import com.google.ar.sceneform.rendering.ViewRenderable
import timber.log.Timber
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException

private const val DP_PER_METERS = 85 //125 // default 250
private const val OFFER_HEIGHT_DP = 105

@RequiresApi(Build.VERSION_CODES.N)
class SpecialOfferNode(private val context: Context?,
                       private val offerModel: LocationOfferDB,
                       private val offerClickLisetener: ((LocationOfferDB) -> Unit)? = null,
                       private val deactivateListener: ((LocationOfferDB) -> Unit)? = null): Node() {

    private var infoCard: InfoCardNode? = null
    private var specialOfferNode: InfoCardNode? = null

    interface OnSpecialOfferClickLisetener {
        fun onOfferClicked(offerModel: LocationOfferDB)
    }

    override fun onActivate() {
        if (scene == null) throw IllegalStateException("Scene is null!")

        if (specialOfferNode == null) {
            val specialOfferStage = ViewRenderable.builder()
                .setView(context, R.layout.special_offer)
                .setSizer(DpToMetersViewSizer(DP_PER_METERS))
                .build()

            val starInfoStage = ViewRenderable.builder()
                .setView(context, R.layout.info_card_view)
                .setSizer(DpToMetersViewSizer(DP_PER_METERS))
                .build()

            CompletableFuture.allOf(specialOfferStage, starInfoStage)
                .handle { _, throwable ->
                    if (throwable != null) {
                        return@handle null
                    }

                    try {
                        specialOfferNode = InfoCardNode()
                        specialOfferNode?.setParent(this)
                        specialOfferNode?.renderable = specialOfferStage.get()

                        //val starBox = specialOfferNode?.collisionShape as? Box?
                        //val starSize = (starBox?.size?.y ?: 0f) * 100 / DP_PER_METERS
                        val starSize = OFFER_HEIGHT_DP.toFloat() / DP_PER_METERS

                        specialOfferNode?.setOnTapListener { hitTestResult, motionEvent ->
                            offerClickLisetener?.invoke(offerModel)
                        }

                        infoCard = InfoCardNode()
                        infoCard?.setParent(specialOfferNode)
                        infoCard?.localPosition = Vector3(0.0f, starSize, 0.0f)

                        val starInfoRenderable: ViewRenderable = starInfoStage.get()
                        infoCard?.renderable = starInfoRenderable
                        val infocardLayout = starInfoRenderable.view
                        val textView: TextView = infocardLayout.findViewById(R.id.tv_info_text)
                        textView.text = offerModel.title

                    } catch (ex: InterruptedException) {
                        Timber.e(ex)
                    } catch (ex: ExecutionException) {
                        Timber.e(ex)
                    }

                    return@handle null
                }
        }
    }

    override fun onDeactivate() {
        offerModel.activated = false
        deactivateListener?.invoke(offerModel)
        super.onDeactivate()
    }
}