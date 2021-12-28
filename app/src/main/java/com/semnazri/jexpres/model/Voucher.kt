package com.semnazri.jexpres.model

import com.google.gson.annotations.SerializedName

data class Voucher(

	@SerializedName("disc35")
	val disc35: Disc35? = null,

	@SerializedName("disc10")
	val disc10: Disc10? = null,

	@SerializedName("ongkir9")
	val ongkir9: Ongkir9? = null,

	@SerializedName("ongkir5")
	val ongkir5: Ongkir5? = null
)