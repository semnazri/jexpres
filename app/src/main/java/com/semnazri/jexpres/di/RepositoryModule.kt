package com.semnazri.jexpres.di

import com.semnazri.jexpres.repository.RepositoryData
import org.koin.dsl.module

val repositoryModule = module {
    single { RepositoryData(get()) }

}