package com.timekeeping.smart.network

import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.entity.request.LoginRequest
import com.timekeeping.smart.entity.response.LoginResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class Repository @Inject constructor(val apiInterface: ApiInterface) {

}