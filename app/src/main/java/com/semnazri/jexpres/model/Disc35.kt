package com.semnazri.jexpres.model

import com.google.gson.annotations.SerializedName

data class Disc35(

	@SerializedName("jenis_potongan")
	val jenisPotongan: String? = "",

	@SerializedName("kode")
	val kode: String? = "",

	@SerializedName("minimum_transaksi")
	val minimumTransaksi: Int? = 0,

	@SerializedName("potongan_minimal")
	val potonganMinimal: Int? = 0,

	@SerializedName("potongan_maksimal")
	val potonganMaksimal: Int? = 0,

	@SerializedName("nilai_persen")
	val nilaiPersen: Int? = 0,

	@SerializedName("status")
	val status: String? = ""
)