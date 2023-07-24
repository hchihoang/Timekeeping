package com.timekeeping.smart.ui.home

import com.timekeeping.smart.BaseApplication
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.entity.response.Decentralization
import com.timekeeping.smart.entity.response.LoginResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.network.Connection_Get_Permission
import com.timekeeping.smart.network.DataCallback
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) :
    BaseViewModel() {
    var logoutLiveData = ObjectResponse<String>()
    var permissionLiveData = ObjectResponse<Boolean>()
    fun logout() {
        hsbaSharePref.logout()
        logoutLiveData.value = BaseObjectResponse<String>().success("")
    }

    fun checkDecentralizationAddLocation(): Boolean {
        val addLocation =
            hsbaSharePref.savedUser?.listDecentralization?.find {
                it.MenuID == Constant.ADD_LOCATION && it.Xem == Constant.VIEW
            }
        return addLocation != null
    }

    fun checkDecentralizationTimeKeeping(): Boolean {
        val addLocation =
            hsbaSharePref.savedUser?.listDecentralization?.find {
                it.MenuID == Constant.TIME_KEEPING && it.Xem == Constant.VIEW
            }
        return addLocation != null
    }

    fun getPermission() {
        permissionLiveData.value = BaseObjectResponse<Boolean>().loading()
        Connection_Get_Permission(hsbaSharePref.savedUser,
        object : DataCallback<ArrayList<Decentralization>> {
                override fun onConnectSuccess(result: ArrayList<Decentralization>) {
                    if(result.size > 0) {
                        val loginResponse = hsbaSharePref.savedUser
                        loginResponse?.listDecentralization = result
                        hsbaSharePref.savedUser = loginResponse
                    }
                    permissionLiveData.value = BaseObjectResponse<Boolean>().success(true)
                }

                override fun onConnectFail(result: ArrayList<Decentralization>?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    permissionLiveData.value = BaseObjectResponse<Boolean>().error(
                        BaseError(stringError),
                        false
                    )
                }
            }
        ).execute()
    }
}