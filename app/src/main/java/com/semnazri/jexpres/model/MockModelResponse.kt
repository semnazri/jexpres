package com.semnazri.jexpres.model

import com.google.gson.annotations.SerializedName

data class MockModelResponse(

	@SerializedName("voucher")
	val voucher: Voucher? = null,

	@SerializedName("name")
	val name: String? = "",

	@SerializedName("location")
	val location: String? = "",

	@SerializedName("products")
	val products: List<ProductsItem> = listOf()
)