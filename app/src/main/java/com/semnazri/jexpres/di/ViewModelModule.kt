package com.semnazri.jexpres.di

import com.semnazri.jexpres.viewModel.CheckoutViewModel
import com.semnazri.jexpres.viewModel.DataViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DataViewModel(get(), get(), get()) }
    viewModel { CheckoutViewModel(get()) }

}