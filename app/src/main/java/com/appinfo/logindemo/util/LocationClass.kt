package com.appinfo.logindemo.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import com.appinfo.logindemo.ui.auth.LoginActivity
import com.google.android.gms.location.*


class LocationClass(private val mContext: Context) {



    lateinit var mFusedLocationClient: FusedLocationProviderClient

@SuppressLint("MissingPermission")
private fun getLastLocation() {

        if (isLocationEnabled()) {

            mFusedLocationClient!!.lastLocation.addOnCompleteListener((mContext as Activity)!!) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    LoginActivity.latitude = location.latitude.toString()
                   LoginActivity.longitude = location.longitude.toString()
                }
            }
        } else {

            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
}

@SuppressLint("MissingPermission")
private fun requestNewLocationData() {
    var mLocationRequest = LocationRequest()
    mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    mLocationRequest.interval = 0
    mLocationRequest.fastestInterval = 0
    mLocationRequest.numUpdates = 1

    mFusedLocationClient = LocationServices.getFusedLocationProviderClient((mContext as Activity)!!)
    mFusedLocationClient!!.requestLocationUpdates(
        mLocationRequest, mLocationCallback,
        Looper.myLooper()
    )
}

private val mLocationCallback = object : LocationCallback() {
    override fun onLocationResult(locationResult: LocationResult) {
        var mLastLocation: Location = locationResult.lastLocation
        LoginActivity.latitude = mLastLocation.latitude.toString()
        LoginActivity.longitude = mLastLocation.longitude.toString()
    }
}

private fun isLocationEnabled(): Boolean {
    var locationManager: LocationManager = mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
        LocationManager.NETWORK_PROVIDER
    )
}

    init {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext)
        getLastLocation()

    }
}