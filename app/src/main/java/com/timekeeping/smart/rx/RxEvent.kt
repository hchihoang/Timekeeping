package com.timekeeping.smart.rx

import com.google.android.gms.location.LocationResult


class RxEvent {
    class CallResponseLocation(var locationResult : LocationResult?)
    class RequestLocation
    class NotifyLocation
}