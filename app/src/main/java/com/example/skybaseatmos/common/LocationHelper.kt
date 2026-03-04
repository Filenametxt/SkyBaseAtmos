package com.example.skybaseatmos.common

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

class LocationHelper(context: Context) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.Builder(10000)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .build()

    fun start(callback: LocationCallback){
        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        } catch (e: SecurityException) {
            // Handle exception
        }
    }
    fun stop(callback: LocationCallback){
        fusedLocationClient.removeLocationUpdates(callback)
    }
}
