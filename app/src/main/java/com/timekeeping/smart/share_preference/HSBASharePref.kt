package com.timekeeping.smart.share_preference

import com.timekeeping.smart.entity.response.LoginResponse
import javax.inject.Singleton

@Singleton
interface HSBASharePref {
    var savedUser: LoginResponse?

    fun logout()

}