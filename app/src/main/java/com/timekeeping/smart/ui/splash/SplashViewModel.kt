package com.timekeeping.smart.ui.splash

import android.app.Application
import com.timekeeping.smart.BaseApplication
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.entity.request.LoginRequest
import com.timekeeping.smart.entity.response.LoginResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.network.Connection_Login
import com.timekeeping.smart.network.DataCallback
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) : BaseViewModel() {

}
