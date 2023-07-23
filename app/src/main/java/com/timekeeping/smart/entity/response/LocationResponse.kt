package com.timekeeping.smart.entity.response

import com.google.gson.annotations.SerializedName

data class LocationResponse(

	@field:SerializedName("MAVT")
	var MAVT: String? = null,

	@field:SerializedName("TENVT")
	var TENVT: String? = null,

	@field:SerializedName("KINHDO")
	var KINHDO: Double? = null,

	@field:SerializedName("VIDO")
	var VIDO: Double? = null,

	@field:SerializedName("GHICHU")
	var GHICHU: String? = null,

	@field:SerializedName("BANKINH")
	var BANKINH: Int? = null
)