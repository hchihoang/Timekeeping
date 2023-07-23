package com.timekeeping.smart.ui.time_keeping

import com.timekeeping.smart.BaseApplication
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.base.entity.BaseListResponse
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.entity.request.DateRequest
import com.timekeeping.smart.entity.request.LocationRequest
import com.timekeeping.smart.entity.response.TimeKeepingResponse
import com.timekeeping.smart.extension.ListResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.network.Connection_Get_Time_Keeping
import com.timekeeping.smart.network.Connection_Set_Time_Keeping
import com.timekeeping.smart.network.DataCallback
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.Constant
import com.timekeeping.smart.utils.DateUtils
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class TimeKeepingViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) :
    BaseViewModel() {
    var timeKeepingResponse = ListResponse<TimeKeepingResponse>()
    var addTimekeepingResponse = ObjectResponse<String>()
    fun getDataTimeKeeping() {
        timeKeepingResponse.value = BaseListResponse<TimeKeepingResponse>().loading()
        val dataRequest = DateRequest(
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1),
            DateUtils.dateToString(Date(), Constant.DATE_FORMAT_1),
            hsbaSharePref.savedUser?.maNV ?: ""
        )
        Connection_Get_Time_Keeping(dataRequest,
            object : DataCallback<List<TimeKeepingResponse>> {
                override fun onConnectSuccess(result: List<TimeKeepingResponse>) {
                    timeKeepingResponse.value = BaseListResponse<TimeKeepingResponse>().success(
                        result
                    )
                }

                override fun onConnectFail(result: List<TimeKeepingResponse>?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    timeKeepingResponse.value = BaseListResponse<TimeKeepingResponse>().error(
                        BaseError(stringError),
                        false
                    )
                }

            }
        ).execute()
    }

    fun getNameUser(): String {
        return hsbaSharePref.savedUser?.name ?: ""
    }

    fun timekeeping(
        longitude: Double?,
        latitude: Double?,
        addressLine: String?
    ) {
        val locationRequest = LocationRequest(
            "", longitude ?: 0.0,
            latitude ?: 0.0, addressLine, DeviceUtil.getDeviceId(BaseApplication.context),
            hsbaSharePref.savedUser?.maNV ?: ""
        )
        Connection_Set_Time_Keeping(locationRequest,
            object : DataCallback<String> {
                override fun onConnectSuccess(result: String) {
                    addTimekeepingResponse.value = BaseObjectResponse<String>().success(result)
                    getDataTimeKeeping()
                }

                override fun onConnectFail(result: String?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    timeKeepingResponse.value = BaseListResponse<TimeKeepingResponse>().error(
                        BaseError(stringError),
                        false
                    )
                }

            }
        ).execute()
    }

}
