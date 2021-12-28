package com.semnazri.jexpres.di

import com.semnazri.jexpres.PreferencesManager
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager }
}