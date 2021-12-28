package com.semnazri.jexpres.di

import android.content.Context
import com.semnazri.jexpres.network.NetworkHelper
import com.semnazri.jexpres.network.NetworkService
import com.semnazri.jexpres.network.createApi
import com.semnazri.jexpres.network.createOkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module


val networkModule = module {
    single { createOkHttpClient() }
    single { createApi<NetworkService>(get()) }
    single { provideNetworkHelper(androidContext()) }

}

private fun provideNetworkHelper(context: Context) = NetworkHelper(context)
