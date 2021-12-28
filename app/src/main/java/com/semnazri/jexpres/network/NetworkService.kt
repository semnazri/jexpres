package com.semnazri.jexpres.network

import com.semnazri.jexpres.model.MockModelResponse
import retrofit2.Response
import retrofit2.http.GET

interface NetworkService {
    @GET("/mobilewarung.json")
    suspend fun getData(): Response<MockModelResponse>
}