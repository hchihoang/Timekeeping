package com.timekeeping.smart.ui.add_location

import com.timekeeping.smart.BaseApplication
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.base.entity.BaseListResponse
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.entity.request.LocationRequest
import com.timekeeping.smart.entity.response.LocationResponse
import com.timekeeping.smart.extension.ListResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.network.Connection_Location
import com.timekeeping.smart.network.Connection_Set_Location
import com.timekeeping.smart.network.DataCallback
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) :
    BaseViewModel() {
    var locationResponse = ListResponse<LocationResponse>()
    var addLocationResponse = ObjectResponse<String>()
    var logoutLiveData = ObjectResponse<String>()

    fun getDataLocation() {
        locationResponse.value = BaseListResponse<LocationResponse>().loading()
        Connection_Location(hsbaSharePref.savedUser?.maNV ?: "",
            object : DataCallback<List<LocationResponse>> {
                override fun onConnectSuccess(result: List<LocationResponse>) {
                    locationResponse.value = BaseListResponse<LocationResponse>().success(
                        result
                    )
                }

                override fun onConnectFail(result: List<LocationResponse>?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    locationResponse.value = BaseListResponse<LocationResponse>().error(
                        BaseError(stringError),
                        false
                    )
                }

            }
        ).execute()
    }

    fun addLocation(
        nameLocation: String,
        longitude: Double?,
        latitude: Double?,
        addressLine: String?
    ) {
        val locationRequest = LocationRequest(
            nameLocation, longitude ?: 0.0,
            latitude ?: 0.0, addressLine, DeviceUtil.getDeviceId(BaseApplication.context),
            hsbaSharePref.savedUser?.maNV ?: ""
        )
        Connection_Set_Location(locationRequest,
            object : DataCallback<String> {
                override fun onConnectSuccess(result: String) {
                    addLocationResponse.value = BaseObjectResponse<String>().success(result)
                    getDataLocation()
                }

                override fun onConnectFail(result: String?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    addLocationResponse.value = BaseObjectResponse<String>().error(
                        BaseError(stringError),
                        false
                    )
                }

            }).execute()
    }

}