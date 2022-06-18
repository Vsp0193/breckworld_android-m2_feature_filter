package com.breckworld.location

import android.annotation.SuppressLint
import android.location.Location
import androidx.lifecycle.LiveData
import com.breckworld.App
import com.google.android.gms.location.*

/**
 * @author Dmytro Bondarenko
 * Date: 04.06.2019
 * Time: 10:15
 * E-mail: bondes87@gmail.com
 */
class CurrentLocationLiveData : LiveData<Location>() {
    private var fusedLocationClient: FusedLocationProviderClient? = null

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            onLocationChanged(locationResult.lastLocation)
        }
    }

    @SuppressLint("MissingPermission")
    override fun onActive() {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(App.applicationContext())
        }
        fusedLocationClient?.lastLocation?.addOnSuccessListener { location ->
            if (location != null) {
                onLocationChanged(location)
            }
        }
        if (hasActiveObservers()) {
            val locationRequest = LocationRequest.create()
            locationRequest.interval = 200
            locationRequest.fastestInterval = 100
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            //fusedLocationClient?.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun updateListeners() {
        onInactive()
        onActive()
    }

    override fun onInactive() {
        fusedLocationClient?.removeLocationUpdates(locationCallback)
        fusedLocationClient = null
    }

    private fun onLocationChanged(location: Location?) {
        value = location
    }

    companion object {

        private var instance: CurrentLocationLiveData? = null

        fun getInstance(): CurrentLocationLiveData {
            if (instance == null) {
                instance = CurrentLocationLiveData()
            }
            return instance as CurrentLocationLiveData
        }
    }
}