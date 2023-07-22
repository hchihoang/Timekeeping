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
    }
}

