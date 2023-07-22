package com.timekeeping.smart.utils

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager

import androidx.core.content.ContextCompat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

import androidx.annotation.IntDef


object PermissionUtil {
    @Retention
    @IntDef(GRANTED, DENIED, BLOCKED_OR_NEVER_ASKED)
    annotation class PermissionStatus

    const val GRANTED = 0
    const val DENIED = 1
    const val BLOCKED_OR_NEVER_ASKED = 2

    @PermissionStatus
    fun getPermissionStatus(activity: Activity, androidPermissionName: String): Int {
        return if (ContextCompat.checkSelfPermission(
                activity,
                androidPermissionName
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    androidPermissionName
                )
            ) {
                BLOCKED_OR_NEVER_ASKED
            } else DENIED
        } else GRANTED
    }

    fun Context.startApplicationSettingsActivity() {
        val intent = Intent()
        val uri: Uri = Uri.fromParts("package", this.packageName, null)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = uri
        this.startActivity(intent)
    }
}