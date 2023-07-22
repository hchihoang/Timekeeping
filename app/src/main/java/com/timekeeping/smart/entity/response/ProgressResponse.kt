package com.timekeeping.smart.entity.response

import com.google.gson.annotations.SerializedName

data class ProgressResponse(

	@field:SerializedName("DUAN")
	var DUAN: String? = null,

	@field:SerializedName("ID_MAY")
	var ID_MAY: String? = null,

	@field:SerializedName("ID_NV")
	var ID_NV: String? = null,

	@field:SerializedName("ID")
	var ID: String? = null,

	@field:SerializedName("NAME")
	var NAME: String? = null,

	@field:SerializedName("TNGAY")
	var TNGAY: String? = null,

	@field:SerializedName("NHOM")
	var NHOM: String? = null,

	@field:SerializedName("TEN_NHOM")
	var TEN_NHOM: String? = null,

	@field:SerializedName("GHICHU")
	var GHICHU: String? = null,

	@field:SerializedName("START_DATE")
	var START_DATE: String? = null,

	@field:SerializedName("END_DATE")
	var END_DATE: String? = null,

	@field:SerializedName("TIENDO")
	var TIENDO: String? = null,

	@field:SerializedName("TRANGTHAI")
	var TRANGTHAI: String? = null,

	@field:SerializedName("DATE_FN")
	var DATE_FN: String? = null,

	@field:SerializedName("DATE_Duration")
	var DATE_Duration: String? = null,

	var isCheck: Boolean = true,
	var isShow: Boolean = true,

	var listDetailProgressRespons: ArrayList<ProgressResponse> = arrayListOf()
)