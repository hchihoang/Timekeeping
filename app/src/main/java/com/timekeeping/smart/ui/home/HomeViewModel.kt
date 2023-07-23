package com.timekeeping.smart.ui.home

import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.share_preference.HSBASharePref
import com.timekeeping.smart.utils.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val hsbaSharePref: HSBASharePref) :
    BaseViewModel() {
    var logoutLiveData = ObjectResponse<String>()
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
}