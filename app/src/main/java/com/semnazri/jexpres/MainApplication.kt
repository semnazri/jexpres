package com.semnazri.jexpres

import android.app.Application
import com.orhanobut.hawk.Hawk
import com.semnazri.jexpres.di.appModule
import com.semnazri.jexpres.di.networkModule
import com.semnazri.jexpres.di.repositoryModule
import com.semnazri.jexpres.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Hawk.init(this).build()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(networkModule, repositoryModule, viewModelModule, appModule)
        }

    }
}