package com.timekeeping.smart.entity.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("username")
	var username: String? = null,

	@field:SerializedName("maNV")
	var maNV: String? = null,

	var name: String? = null,

	var listDecentralization: ArrayList<Decentralization>? = null
)

data class Decentralization (var ID: String? = null, var MenuID: String? = null,
							 var UserID: String? = null, var Xem: Int? = 0,
							 var NAME_VN: String? = null)