package com.timekeeping.smart.extension

import androidx.lifecycle.MutableLiveData
import com.timekeeping.smart.R
import com.timekeeping.smart.base.entity.BaseListLoadMoreResponse
import com.timekeeping.smart.base.entity.BaseListResponse
import com.timekeeping.smart.base.entity.BaseObjectResponse

typealias ObjectResponse<T> = MutableLiveData<BaseObjectResponse<T>>
typealias ListResponse<T> = MutableLiveData<BaseListResponse<T>>
typealias ListLoadMoreResponse<T> = MutableLiveData<BaseListLoadMoreResponse<T>>

typealias AndroidColors = android.R.color
typealias ProjectColors = R.color
