package com.timekeeping.smart.entity.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("username")
	var username: String? = null,

	@field:SerializedName("maNV")
	var maNV: String? = null,
)