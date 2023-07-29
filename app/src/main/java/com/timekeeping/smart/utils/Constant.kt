package com.timekeeping.smart.utils

import android.Manifest

class Constant {
    companion object {
        const val DATE_FORMAT_1 = "MM/dd/yyyy"
        const val DATE_FORMAT_2 = "dd/MM/yyyy"
        const val DATE_FORMAT_3 = "yyyy-MM-dd"
        val REQUIRED_PERMISSIONS_LOCATION = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        const val VIEW = 1
        const val ADD_LOCATION = "1"
        const val TIME_KEEPING = "2"
        const val STORE_RESULT_OK = "OK"
        val REQUIRED_PERMISSIONS_READ_ANDROID_33 = arrayOf(
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.CAMERA
        )
        val REQUIRED_PERMISSIONS_READ = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        const val REQUEST_IMAGE_CAPTURE = 1888
    }
}

