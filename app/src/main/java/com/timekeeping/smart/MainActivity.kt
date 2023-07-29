package com.timekeeping.smart

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.timekeeping.smart.base.BaseActivity
import com.timekeeping.smart.rx.RxBus
import com.timekeeping.smart.rx.RxEvent
import com.timekeeping.smart.ui.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private var isRxRequestLocation = false
    override val layoutResId: Int
        get() = R.layout.activity_main
    override val layoutId: Int
        get() = R.id.container

    @SuppressLint("CheckResult")
    override fun initListener() {
        RxBus.listen(RxEvent.RequestLocation::class.java)
            .subscribe(
                {
                    isRxRequestLocation = true
                    requestLocationUpdate()
                },
                {

                }
            )
    }

    override fun initView() {
        getViewController().addFragment(SplashFragment::class.java)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                RxBus.locationResult.onNext(RxEvent.CallResponseLocation(locationResult))
                if(isRxRequestLocation){
                    isRxRequestLocation = false
                    RxBus.publish(RxEvent.NotifyLocation())
                }
            }
        }
        createLocationRequest()
    }

    @SuppressLint("VisibleForTests")
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest?.interval = 0
        mLocationRequest?.fastestInterval = 0
        mLocationRequest?.smallestDisplacement = 5F
        requestLocationUpdate()
    }

    private fun requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mLocationRequest?.let {
            mLocationCallback?.let { it1 ->
                mFusedLocationClient?.requestLocationUpdates(
                    it,
                    it1,
                    Looper.myLooper()
                )
            }
        }
    }

    override fun initData() {
    }

    override fun onDestroy() {
        super.onDestroy()
        mLocationCallback?.let { mFusedLocationClient?.removeLocationUpdates(it) }
        RxBus.locationResult.onNext(RxEvent.CallResponseLocation(null))
    }

    override fun onPause() {
        super.onPause()
        mLocationCallback?.let { mFusedLocationClient?.removeLocationUpdates(it) }
        RxBus.locationResult.onNext(RxEvent.CallResponseLocation(null))
    }

    override fun onResume() {
        super.onResume()
        requestLocationUpdate()
    }

}
