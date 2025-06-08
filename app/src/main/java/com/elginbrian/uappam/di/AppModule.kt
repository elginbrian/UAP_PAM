package com.elginbrian.uappam.di

import com.elginbrian.uappam.data.remote.ApiConfig
import com.elginbrian.uappam.data.repositoty.PlantRepository
import com.elginbrian.uappam.presentation.add_item.AddItemViewModel
import com.elginbrian.uappam.presentation.detail.DetailViewModel
import com.elginbrian.uappam.presentation.edit_item.EditItemViewModel
import com.elginbrian.uappam.presentation.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { ApiConfig.getApiService() }
}

val repositoryModule = module {
    single { PlantRepository(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { AddItemViewModel(get()) }
    viewModel { DetailViewModel(get()) }
    viewModel { EditItemViewModel(get()) }
}

val appModules = listOf(networkModule, repositoryModule, viewModelModule)