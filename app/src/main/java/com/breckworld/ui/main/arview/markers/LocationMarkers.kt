package com.breckworld.ui.main.arview.markers

import android.location.Location
import android.location.LocationManager
import com.breckworld.App
import com.breckworld.R
import com.breckworld.extensions.toPx
import com.breckworld.repository.database.model.LocationOfferDB
import com.breckworld.repository.database.model.LocationStarDB
import com.breckworld.repository.database.model.OfferDB
import com.breckworld.repository.local.model.MarkerModel
import com.breckworld.repository.remote.http.model.Offer
import com.breckworld.ui.main.arview.compass.MathHelpers
import com.directions.route.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import timber.log.Timber
import uk.co.appoly.arcorelocation.utils.LocationUtils
import kotlin.math.abs

private const val ROUTE_UPDATE_DISPLACEMENT = 20.0
private const val ROUTE_UPDATE_MINIMUM_ACCURACY = 20f
private const val EARTH_RADIUS = 6378100.0
private const val SCENE_MAX_DISTANCE = 30.0
private const val SCENE_DISTANCE = 20.0
private const val SCENE_MINIMUM_ACCURACY = 20f
private const val MAXIMUM_SETUP_MARKER_DISTANCE = 20f

class LocationMarkers(private var googleMap: GoogleMap?,
                      private val markerIcon: BitmapDescriptor,
                      var locationChanged: LocationChanged? = null) {

    private val markers: MutableList<LocationStarDB> = mutableListOf()
    private val mMarkerMap: MutableMap<LocationStarDB, Marker> = mutableMapOf()
    private var routePolyLineOptions: PolylineOptions? = null
    private var routePolyLine: Polyline? = null
    private var lastNearestMarkerModel: LocationStarDB? = null
    private var offers: List<LocationOfferDB> = mutableListOf()
    private var lastNearestOfferOrMarkerModel: MarkerModel? = null
    private var lastLocation: Location? = null

    @Volatile
    private var sceneCreated: Boolean = false

    private var setupMarkers: MutableList<MarkerModel> = mutableListOf()

    private var lastRouteLocation: LatLng? = null

    @Volatile private var isUpdating = false

    private var routeNeedUpdate = false

    init {
        initRoute()
    }

    fun addMarker(marker: LocationStarDB) {
        val findedMarker = markers.find { it.id == marker.id }
        if (findedMarker == null) {
            markers.add(marker)
            addGoogleMarker(marker)
        } else if (findedMarker != marker) {
            removeMarker(findedMarker)
            markers.add(marker)
            addGoogleMarker(marker)
        }
    }

    fun addMarkers(markers: List<LocationStarDB>) {
        for (marker in markers) addMarker(marker)
    }

    fun setOffers(offers: List<LocationOfferDB>) {
        this.offers = offers
    }

    private fun addGoogleMarker(marker: LocationStarDB) {
        val markerOptions = MarkerOptions()
            .icon(markerIcon)
            .position(LatLng(marker.lat, marker.lng))
            .anchor(0.5f, 0.5f)

        val googleMarker = googleMap?.addMarker(markerOptions) ?: return
        mMarkerMap[marker] = googleMarker
    }

    fun removeMarker(marker: LocationStarDB) {
        markers.remove(marker)
        val googleMarker = mMarkerMap[marker]
        googleMarker?.remove()
        mMarkerMap.remove(marker)
    }

    fun removeMarkerById(id: Long) {
        val marker = markers.find { it.id == id }
        marker?.let {
            removeMarker(it)
        }
    }

    fun clearScene() {
        markers.forEach {
            it.activated = false
            it.distanceToMarker = 0.0
            it.bearingToMarker = 0f
        }
        offers.forEach {
            it.activated = false
            it.distanceToMarker = 0.0
            it.bearingToMarker = 0f
        }
        sceneCreated = false
    }

    fun setCurrentLocation(currentLocation: Location, bearing: Float) {
        var minimalDistance = EARTH_RADIUS
        markers.forEach {
            it.distanceToMarker = LocationUtils.distance(it.lat, currentLocation.latitude,
                it.lng, currentLocation.longitude, 0.0, 0.0)

            if (it.distanceToMarker < minimalDistance && it.distanceToMarker > 0.0) {
                minimalDistance = it.distanceToMarker
            }

            val currentMarkerLocation = Location(LocationManager.GPS_PROVIDER)
            currentMarkerLocation.longitude = it.lng
            currentMarkerLocation.latitude = it.lat
            currentMarkerLocation.altitude = currentLocation.altitude

            it.bearingToMarker = MathHelpers.bearingDiff(bearing, currentLocation.bearingTo(currentMarkerLocation))
        }
        offers.forEach {
            it.distanceToMarker = LocationUtils.distance(it.pickLat, currentLocation.latitude,
                it.pickLng, currentLocation.longitude, 0.0, 0.0)

            if (it.distanceToMarker < minimalDistance && it.distanceToMarker > 0.0) {
                minimalDistance = it.distanceToMarker
            }

            val currentMarkerLocation = Location(LocationManager.GPS_PROVIDER)
            currentMarkerLocation.longitude = it.pickLng
            currentMarkerLocation.latitude = it.pickLat
            currentMarkerLocation.altitude = currentLocation.altitude

            it.bearingToMarker = MathHelpers.bearingDiff(bearing, currentLocation.bearingTo(currentMarkerLocation))
        }
        if (currentLocation != lastLocation) {
            if (currentLocation.accuracy < ROUTE_UPDATE_MINIMUM_ACCURACY) {
                checkAndUpdateRoute(LatLng(currentLocation.latitude, currentLocation.longitude))
            }
            if (currentLocation.accuracy < SCENE_MINIMUM_ACCURACY &&
                minimalDistance < SCENE_DISTANCE &&
                !sceneCreated
            ) {
                sceneCreated = locationChanged?.onSceneCreated() ?: true
            }
            if (minimalDistance >= SCENE_MAX_DISTANCE && sceneCreated) {
                sceneCreated = false
                locationChanged?.onSceneCleared()
            }
            locationChanged?.onLocationChanged(currentLocation)
            lastLocation = currentLocation
        }
    }

    fun getMarkersForSetupScene(): List<MarkerModel> {
        setupMarkers.clear()
        val filteredMarkers = markers.filter { !it.activated &&
                    it.distanceToMarker <= MAXIMUM_SETUP_MARKER_DISTANCE &&
                    it.distanceToMarker > 0.0 &&
                    isCameraAngleReasonable(it.distanceToMarker, it.bearingToMarker)
        }
        setupMarkers.addAll(filteredMarkers)
        val filteredOffers = offers.filter { !it.activated &&
                it.distanceToMarker <= MAXIMUM_SETUP_MARKER_DISTANCE &&
                it.distanceToMarker > 0.0 &&
                isCameraAngleReasonable(it.distanceToMarker, it.bearingToMarker)
        }
        setupMarkers.addAll(filteredOffers)
        return setupMarkers
    }

    fun getActivatedMarkers(): String =
        markers.filter { it.activated }.map { it.title }
            .union(offers.filter { it.activated }.map { it.title }).joinToString(separator = ", ")

    fun getMinimalDistance(): Double? {
        var min_distance_set = markers.map { it.distanceToMarker }
            .union(offers.map { it.distanceToMarker })

        if ( min_distance_set == null ){
            return 0.0;
        }

        return min_distance_set.minOrNull();
    }

    private fun isCameraAngleReasonable(distance: Double, angle: Float): Boolean {
        return when {
            distance > 5.0 -> abs(angle) < 15f
            distance in 1.0..5.0 -> abs(angle) < 60f
            distance in 0.0..1.0 -> true
            else -> false
        }
    }

    fun setupMap(googleMap: GoogleMap?) {
        clearMapAndMarkers()

        this.googleMap = googleMap
        for (marker in markers) {
            addGoogleMarker(marker)
        }
        initRoute()
    }

    fun clearMapAndMarkers() {
        for (markerEntry in mMarkerMap) {
            markerEntry.value.remove()
        }
        mMarkerMap.clear()
        routePolyLine = null
        routePolyLineOptions = null
        googleMap?.clear()
        googleMap = null
    }

    private fun initRoute() {
        routePolyLineOptions = PolylineOptions()
            .width(4f.toPx().toFloat())
            .color(App.getResColor(R.color.colorRouteMap))
            .geodesic(false)

        routePolyLine = googleMap?.addPolyline(routePolyLineOptions)
        routePolyLine?.zIndex = 1000f
        routePolyLine?.isVisible = true
    }

    fun checkAndUpdateRoute(currentLocation: LatLng) {
        val nearestOfferOrMarker = getNearestOfferOrMarker(currentLocation)
        if (nearestOfferOrMarker != lastNearestOfferOrMarkerModel) {
            lastNearestOfferOrMarkerModel = nearestOfferOrMarker
        }

        val nearestMarker = getNearestMarker(currentLocation)

        if (nearestMarker != lastNearestMarkerModel) {
            routeNeedUpdate = true
            lastNearestMarkerModel = nearestMarker
        }
        if (lastRouteLocation == null) {
            routeNeedUpdate = true
        }
        lastRouteLocation?.let {
            val displacement = Math.abs(
                LocationUtils.distance(it.latitude,
                    currentLocation.latitude,
                    it.longitude,
                    currentLocation.longitude,
                    0.0,
                    0.0)
            )
            if (displacement > ROUTE_UPDATE_DISPLACEMENT) routeNeedUpdate = true
        }

        if (routeNeedUpdate) updateRoute(currentLocation)
    }

    private fun updateRoute(currentLocation: LatLng) {
        Timber.d("Route update!!!")
        if (lastNearestMarkerModel != null) {
            val lastNearestMarkerLatLng = LatLng(lastNearestMarkerModel?.lat ?: 0.0, lastNearestMarkerModel?.lng ?: 0.0)

            if (!isUpdating) {
                routeNeedUpdate = false
                val routing = Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(object : RoutingListener {
                        override fun onRoutingFailure(e: RouteException) {
                            val points = mutableListOf<LatLng>()
                            points.add(currentLocation)
                            points.add(lastNearestMarkerLatLng)
                            routePolyLine?.points = points
                            //routeNeedUpdate = true
                            routeNeedUpdate = false
                            lastRouteLocation = currentLocation
                            isUpdating = false
                        }

                        override fun onRoutingStart() {

                        }

                        override fun onRoutingSuccess(arrayList: ArrayList<Route>, i: Int) {
                            val bestRoute = arrayList[i]
                            val points = bestRoute.points
                            points.add(0, currentLocation)
                            lastRouteLocation = currentLocation
                            routePolyLine?.points = points
                            routeNeedUpdate = false
                            isUpdating = false
                        }

                        override fun onRoutingCancelled() {

                        }
                    })
                    .waypoints(currentLocation, lastNearestMarkerLatLng)
                    .key(App.getStringFromRes(R.string.google_maps_key))
                    .build()
                routing.execute()
            }
        } else {
            routePolyLine?.points = emptyList()
        }
    }

    private fun getNearestMarker(curentLatLng: LatLng): LocationStarDB? {
        var minimalDistance = Double.MAX_VALUE
        var bestMarker: LocationStarDB? = null
        for (marker in markers) {
            val distance = LocationUtils.distance(curentLatLng.latitude, marker.lat, curentLatLng.longitude, marker.lng, 0.0, 0.0)
            if (distance < minimalDistance) {
                minimalDistance = distance
                bestMarker = marker
            }
        }
        return bestMarker
    }

    private fun getNearestOfferOrMarker(currentLatLng: LatLng): MarkerModel? {
        var minimalDistance = Double.MAX_VALUE
        var bestMarker: MarkerModel? = null
        for (marker in markers) {
            val distance = LocationUtils.distance(currentLatLng.latitude, marker.lat, currentLatLng.longitude, marker.lng, 0.0, 0.0)
            if (distance < minimalDistance) {
                minimalDistance = distance
                bestMarker = marker
            }
        }
        for (offer in offers) {
            val distance = LocationUtils.distance(currentLatLng.latitude, offer.pickLat, currentLatLng.longitude, offer.pickLng, 0.0, 0.0)
            if (distance < minimalDistance) {
                minimalDistance = distance
                bestMarker = offer
            }
        }
        return bestMarker
    }

    fun getCurrentMarker(): LocationStarDB? = lastNearestMarkerModel
    fun getCurrentOfferOrMarker(): MarkerModel? = lastNearestOfferOrMarkerModel

    interface LocationChanged {
        fun onLocationChanged(location: Location)
        fun onSceneCreated(): Boolean
        fun onSceneCleared()
    }
}