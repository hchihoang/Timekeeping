package com.timekeeping.smart.ui.login

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
class LoginViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) : BaseViewModel() {
    var loginResponse = ObjectResponse<LoginResponse>()
    fun login(userName: String, passWork: String) {
        loginResponse.value = BaseObjectResponse<LoginResponse>().loading()
        if(userName.isBlank() || passWork.isBlank()){
            loginResponse.value = BaseObjectResponse<LoginResponse>().error(
                BaseError(R.string.str_validate_user_pass_work),
                false
            )
            return
        }
        val loginRequest = LoginRequest(userName, passWork,
            DeviceUtil.getDeviceId(BaseApplication.context))
        Connection_Login(loginRequest,
            object : DataCallback<LoginResponse> {
                override fun onConnectSuccess(result: LoginResponse) {
                    hsbaSharePref.savedUser = result
                    loginResponse.value = BaseObjectResponse<LoginResponse>().success(result)
                }

                override fun onConnectFail(result: LoginResponse?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    }else {
                        stringError = R.string.str_error_get_data
                    }
                    loginResponse.value = BaseObjectResponse<LoginResponse>().error(
                        BaseError(stringError),
                        false
                    )
                }
            }
        ).execute()
    }

    fun isLogin(): Boolean{
        if(hsbaSharePref.savedUser?.maNV.isNullOrEmpty()){
            return false
        }
        return true
    }

}
