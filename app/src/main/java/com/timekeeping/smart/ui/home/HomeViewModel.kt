package com.timekeeping.smart.ui.home

import com.timekeeping.smart.base.BaseViewModel
import com.timekeeping.smart.base.entity.BaseObjectResponse
import com.timekeeping.smart.extension.ObjectResponse
import com.timekeeping.smart.share_preference.HSBASharePref
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

}