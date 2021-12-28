package com.semnazri.jexpres.model

import com.google.gson.annotations.SerializedName

data class ProductsItem(

	@SerializedName("price")
	val price: Int? = 0,

	@SerializedName("name")
	val name: String? = "",

	@SerializedName("category")
	val category: String? = ""
)