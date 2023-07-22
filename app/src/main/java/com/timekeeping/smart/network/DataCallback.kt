package com.timekeeping.smart.network


interface DataCallback<T> {
    fun onConnectSuccess(result: T)
    fun onConnectFail(result: T?)
}
