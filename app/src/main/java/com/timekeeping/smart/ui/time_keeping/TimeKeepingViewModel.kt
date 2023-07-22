package com.timekeeping.smart.ui.time_keeping

import com.timekeeping.smart.BaseApplication
import com.timekeeping.smart.R
import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseError
import com.timekeeping.smart.base.entity.BaseListResponse
import com.timekeeping.smart.entity.request.DateRequest
import com.timekeeping.smart.entity.response.ProgressResponse
import com.timekeeping.smart.extension.ListResponse
import com.timekeeping.smart.network.Connection_Progress
import com.timekeeping.smart.network.DataCallback
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.DeviceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TimeKeepingViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) : BaseViewModel() {
    var dataProgressResponse = ListResponse<ProgressResponse>()
    fun getDataProgress(data: DateRequest) {
        data.maNV = hsbaSharePref.savedUser?.maNV
        dataProgressResponse.value = BaseListResponse<ProgressResponse>().loading()
        Connection_Progress(data,
            object : DataCallback<List<ProgressResponse>> {
                override fun onConnectSuccess(result: List<ProgressResponse>) {
                    val listProgressResponse: ArrayList<ProgressResponse> = convertData(result)
                    dataProgressResponse.value = BaseListResponse<ProgressResponse>().success(
                        listProgressResponse
                    )
                }

                override fun onConnectFail(result: List<ProgressResponse>?) {
                    val stringError: Int
                    if (!DeviceUtil.hasConnection(BaseApplication.context)) {
                        stringError = R.string.str_error_connect_internet
                    } else {
                        stringError = R.string.str_error_get_data
                    }
                    dataProgressResponse.value = BaseListResponse<ProgressResponse>().error(
                        BaseError(stringError),
                        false
                    )
                }

            }
        ).execute()
    }

    private fun convertData(result: List<ProgressResponse>): ArrayList<ProgressResponse> {
        val listProgressResponse: ArrayList<ProgressResponse> = arrayListOf()
        for (progressResponse in result) {
            if (progressResponse.NHOM == "0" || progressResponse.NHOM.isNullOrEmpty()) {
                listProgressResponse.add(progressResponse)
            }
        }
        if (listProgressResponse.size > 0) {
            addDataItem(listProgressResponse, result)
        }
        return listProgressResponse
    }

    private fun addDataItem(
        listProgressResponse: ArrayList<ProgressResponse>,
        result: List<ProgressResponse>
    ) {
        for (progressResponse in listProgressResponse) {
            for (item in result) {
                if (progressResponse.ID == item.NHOM) {
                    progressResponse.listDetailProgressRespons.add(item)
                }
            }
            if (progressResponse.listDetailProgressRespons.size > 0) {
                addDataItem(progressResponse.listDetailProgressRespons, result)
            }
        }
    }

}
