package com.semnazri.jexpres.repository

import com.semnazri.jexpres.model.MockModelResponse
import com.semnazri.jexpres.network.NetworkService
import com.semnazri.jexpres.network.ResultCall
import com.semnazri.jexpres.network.callAwait

class RepositoryData(private val networkService: NetworkService) {

    suspend fun requestMockData(): ResultCall<MockModelResponse> {
        return networkService.getData().callAwait()
    }
}