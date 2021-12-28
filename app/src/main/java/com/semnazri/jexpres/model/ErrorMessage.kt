package com.semnazri.jexpres.model

import com.google.gson.annotations.SerializedName

data class ErrorMessage(
    @SerializedName("success") val success: Boolean? = false,
    @SerializedName("message") val message: String? = "",
    @SerializedName("code") val code: Int? = 0
)