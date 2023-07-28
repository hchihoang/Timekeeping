package com.timekeeping.smart.entity.response

import com.google.gson.annotations.SerializedName

data class TimeKeepingResponse(

	@field:SerializedName("ID_NV")
	var ID_NV: String? = null,

	@field:SerializedName("MAVT")
	var MAVT: String? = null,

	@field:SerializedName("TRX_DATE")
	var TRX_DATE: String? = null,

	@field:SerializedName("GIOCHAM")
	var GIOCHAM: String? = null,

	@field:SerializedName("THIETBI")
	var THIETBI: String? = null,

	@field:SerializedName("kd")
	var KINHDO: Double? = null,

	@field:SerializedName("vd")
	var VIDO: Double? = null
)