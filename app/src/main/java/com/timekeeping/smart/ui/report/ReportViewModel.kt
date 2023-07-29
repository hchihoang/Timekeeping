package com.timekeeping.smart.ui.report

import android.graphics.Bitmap
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.share_preference.HSBASharePref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) :
    BaseViewModel() {
    var imageReport: Bitmap? = null


}