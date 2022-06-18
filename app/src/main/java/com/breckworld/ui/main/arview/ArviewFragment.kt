package com.breckworld.ui.main.arview

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.breckworld.R
import com.breckworld.architecture.BaseMVVMFragment
import com.breckworld.livedata.EventObserver
import com.breckworld.databinding.FragmentArviewBinding
import com.breckworld.extensions.Constants
import com.breckworld.extensions.navigateTo
import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.local.model.MarkerModel
import com.breckworld.ui.main.arview.arcore.NFArFragment
import com.breckworld.ui.main.arview.arcore.SpecialOfferNode
import com.breckworld.ui.main.arview.arcore.StarNode
import com.breckworld.ui.main.arview.compass.*
import com.breckworld.ui.main.arview.markers.LocationMarkers
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.ar.core.*
import com.google.ar.sceneform.*
import com.google.ar.sceneform.Camera
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.Texture
import kotlinx.android.synthetic.main.fragment_arview.*
import permissions.dispatcher.*
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity
import timber.log.Timber
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import kotlin.math.atan2

@RuntimePermissions
class ArviewFragment : BaseMVVMFragment<ArviewViewModel, FragmentArviewBinding, ArviewFragment.Events>(),
    OnMapReadyCallback, Scene.OnUpdateListener {

    var googleMap: GoogleMap? = null
    var maxZoomLevel: Float = 0f

    private lateinit var arFragment: NFArFragment
    private lateinit var arSceneView: ArSceneView
    var guideView: GuideView? = null

    var starRenderable: ModelRenderable? = null

    var kalmanGyroscopeSensorLiveData: KalmanGyroscopeSensorLiveData? = null

    private var locationMarkers: LocationMarkers? = null

    override fun viewModelClass(): Class<ArviewViewModel> {
        return ArviewViewModel::class.java
    }

    override fun layoutResId(): Int {
        return R.layout.fragment_arview
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            kalmanGyroscopeSensorLiveData = KalmanGyroscopeSensorLiveData(it)
            kalmanGyroscopeSensorLiveData?.observe(this, Observer { sensorData ->
                viewModel.currentLocation.value?.let {
                    locationMarkers?.setCurrentLocation(
                        it, ((Math.toDegrees(sensorData[2].toDouble()).toFloat() + 360f) % 360f) - 180f
                    )
                }
            })
        }
        viewModel.presentStars.observe(this, Observer { stars ->
            stars?.let {
                locationMarkers?.addMarkers(it)
            }
        })
        viewModel.collectedStar.observe(this, Observer { star ->
            star?.let {
                val bundle = bundleOf(Constants.KEY_ANY to it, Constants.KEY_LOCATION to viewModel.currentLocation.value)
                findNavController().navigateTo(R.id.action_global_to_star_collect_dialog, bundle)
            }
        })
        viewModel.offers.observe(this, Observer { offers ->
            offers?.let {
                locationMarkers?.setOffers(it)
            }
        })
        viewModel.currentLocation.observe(this, Observer {
            viewModel.checkAndUpdateStars(false)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        showBottomNavigation(true)
        setupStatusbar(true, false)
        showGuide()
    }

    override fun onDestroyView() {
        locationMarkers?.clearMapAndMarkers()
        locationMarkers?.clearScene()
        clearScene()
        guideView?.dismiss()
        super.onDestroyView()
    }

    private fun showGuide() {
        if (viewModel.isGuideArView()) {
            Handler().postDelayed({
                mapGuide()
            }, Constants.GUIDE_DELAY)
        }
    }

    private fun mapGuide() {
        guideView = GuideView.Builder(context)
            .setTitle(getString(R.string.guide_arview_1))
            .setTitleTextSize(14)
            .setGravity(Gravity.center)
            .setTargetView(view_guide_map)
            .setDismissType(DismissType.insideMessageView)
            .setGuideListener {
                if (isVisible) {
                    viewModel.saveGuideArView(false)
                }
            }
            .build()
        guideView?.show()
    }

    private fun checkArCoreSupportAndInit() {
        val arcoreAvailability = ArCoreApk.getInstance().checkAvailability(context)
        if (arcoreAvailability.isTransient) {
            Handler().postDelayed({
                checkArCoreSupportAndInit()
            }, 200)
        } else if (arcoreAvailability.isSupported) {
            initArCore()
        } else {
            arcoreUnsupported()
        }
    }

    private fun initArCore() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            val starTextureStage = Texture.builder().setSource(context, R.drawable.star3d)
                .build()
                .thenCompose { texture ->
                    MaterialFactory.makeOpaqueWithTexture(context, texture)
                }

            val starModelRenderableStage = ModelRenderable.builder()
                .setSource(context, Uri.parse("star3d.sfb"))
                .build()

            CompletableFuture.allOf(
                starTextureStage,
                starModelRenderableStage
            )
                .handle { _, throwable ->
                    if (throwable != null) {
                        showToast("Unable to load renderable")
                        return@handle null
                    }

                    try {
                        starRenderable = starModelRenderableStage.get()
                        starRenderable?.material = starTextureStage.get()
                    } catch (ex: InterruptedException) {
                        showToast("Unable to load renderable")
                    } catch (ex: ExecutionException) {
                        showToast("Unable to load renderable")
                    }

                    return@handle null
                }
        } else {
            arcoreUnsupported()
        }
    }

    private fun arcoreUnsupported() {
        showToast("Unsupported")
    }

    override fun subscribeToEvents() {
        viewModel.eventsLiveData.observe(this, EventObserver {
            when (it) {
                Events.BACK -> findNavController().navigateUp()
            }
        })
    }

    @SuppressLint("NewApi")
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        mapFragment.view?.alpha = 0.8f

        maxZoomLevel = googleMap?.maxZoomLevel ?: 0f

        if (locationMarkers == null) {
            locationMarkers = LocationMarkers(googleMap, getMarkerIcon(), object : LocationMarkers.LocationChanged {
                override fun onLocationChanged(location: Location) {

                }

                override fun onSceneCreated(): Boolean {
                    starRenderable?.let {
                        arSceneView.scene.addOnUpdateListener(this@ArviewFragment)
                    }
                    return starRenderable != null
                }

                override fun onSceneCleared() {
                    clearScene()
                }
            })
        } else {
            locationMarkers?.setupMap(googleMap)
        }

        enableLocationWithPermissionCheck()
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    fun enableLocation() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context!!,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        stub_fragment.setOnInflateListener { stub, inflated ->
            checkArCoreSupportAndInit()
            arFragment = childFragmentManager.findFragmentById(R.id.ux_fragment) as NFArFragment
            arSceneView = arFragment.arSceneView
        }
        stub_fragment.inflate()

        googleMap?.isMyLocationEnabled = true

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        fusedLocationProviderClient
            .lastLocation
            .addOnSuccessListener { location ->
                if (activity == null) return@addOnSuccessListener
                if (location == null) {
                    Timber.d("Location is null")
                    return@addOnSuccessListener
                }
                val latLng = LatLng(location.latitude, location.longitude)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, CAMERA_DEFAULT_ZOOM)

                googleMap?.animateCamera(cameraUpdate)
            }

        googleMap?.isMyLocationEnabled = true

        viewModel.currentLocation.updateListeners()
    }

    private fun clearScene() {
        if (::arSceneView.isInitialized) {
            val nodes: MutableList<Node> = mutableListOf()
            nodes.addAll(arSceneView.scene.children)
            for (node in nodes) {
                if (node is AnchorNode) node.anchor?.detach()
                if ((node !is Camera) && (node !is Sun)) node.setParent(null)
            }
            arSceneView.scene.removeOnUpdateListener(this)
        }
    }

    private fun getMarkerIcon(): BitmapDescriptor {
        val imageBitmap = BitmapFactory.decodeResource(
            resources,
            resources.getIdentifier("ic_star_map_nearest", "drawable", context!!.getPackageName())
        )
        val width = (imageBitmap.width * COEFFICIENT_TRANSFORM).toInt()
        val height = (imageBitmap.height * COEFFICIENT_TRANSFORM).toInt()
        val resizedBitmap = Bitmap.createScaledBitmap(
            imageBitmap,
            width, height, false
        )
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onUpdate(frameTime: FrameTime?) {
        val frame = arSceneView.arFrame

        if (frame != null) {
            var trackingState = false
            val updateTrackables = frame.getUpdatedTrackables(Plane::class.java).iterator()

            while (updateTrackables.hasNext()) {
                val plane = updateTrackables.next() as Plane
                if (plane.trackingState == TrackingState.TRACKING) {
                    arFragment.planeDiscoveryController.hide()
                    trackingState = true
                }
            }

            if (trackingState) {
                val markers = locationMarkers?.getMarkersForSetupScene() ?: return
                markers.forEach {
                    val anchor = createMarkerAnchor(it.distanceToMarker)
                    if (anchor != null) createMarkerNode(anchor, it)
                }
            }
        }
    }

    private fun createMarkerAnchor(markerDistance: Double): Anchor? {
        val cameraRelativePose = Pose.makeTranslation(0f, 0f, -markerDistance.toFloat())

        val frame = arSceneView.arFrame
        val camera = frame?.camera
        val cameraPose = frame?.androidSensorPose?.compose(cameraRelativePose)?.extractTranslation()

        val forwardDirection = FloatArray(3)
        val forwardVector = floatArrayOf(0f, 0f, -1f)
        camera?.pose?.rotateVector(forwardVector, 0, forwardDirection, 0)
        val downPose = cameraPose?.compose(
            MathHelpers.axisRotation(
                1,
                -atan2(forwardDirection[0].toDouble(), forwardDirection[2].toDouble()).toFloat()
            )
        )

        val cameraHeight = camera?.pose?.ty() ?: 0f // height
        val poseHeight = downPose?.ty() ?: 0f
        val correctionHeight = cameraHeight - poseHeight

        val position = floatArrayOf(0f, correctionHeight, 0f)
        val rotation = floatArrayOf(0f, 0f, 0f, 1f)
        val shellPose = downPose?.compose(Pose(position, rotation))

        return arSceneView.session?.createAnchor(shellPose)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createMarkerNode(modelAnchor: Anchor, markerModel: MarkerModel) {
        //Attach a node to this anchor with the scene as the parent
        val anchorNode = AnchorNode(modelAnchor)
        anchorNode.setParent(arSceneView.scene)

        val markerNode = when (markerModel) {
            is LocationStarDB ->
                StarNode(
                    context,
                    markerModel,
                    starRenderable,
                    { star ->
                        locationMarkers?.removeMarker(star)
                        modelAnchor.detach()
                        viewModel.collectStar(star)
                    })
            is LocationOfferDB ->
                SpecialOfferNode(
                    context,
                    markerModel,
                    { offer ->
                        val currentLocation = viewModel.currentLocation.value
                        val bundle = bundleOf(
                            Constants.KEY_OFFER to offer,
                            Constants.KEY_LOCATION to currentLocation
                        )
                        findNavController().navigateTo(R.id.action_global_to_offer_dialog, bundle)
                    })
            else -> return
        }

        markerNode.setParent(anchorNode)
        markerModel.activated = true
        markerNode.addLifecycleListener(object : Node.LifecycleListener {
            override fun onDeactivated(node: Node?) {
                modelAnchor.detach()
            }

            override fun onActivated(node: Node?) {}
            override fun onUpdated(node: Node?, frameTime: FrameTime?) {}
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    enum class Events {
        BACK
    }

    companion object {
        const val COEFFICIENT_TRANSFORM = 0.8
        const val CAMERA_DEFAULT_ZOOM = 18f

        fun newInstance(args: Bundle? = null): ArviewFragment {
            val fragment = ArviewFragment()
            fragment.arguments = args
            return fragment
        }
    }
}