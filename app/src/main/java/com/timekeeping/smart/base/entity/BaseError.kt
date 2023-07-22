package com.timekeeping.smart.base.entity

import androidx.annotation.StringRes

data class BaseError(@StringRes var error: Int, var code: Int = 1) : Exception("")