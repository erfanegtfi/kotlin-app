package com.e.kotlinapp.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.kotlinapp.network.coroutine.CategoryViewModelCoroutine
import com.e.kotlinapp.network.flow.CategoryViewModelFlow
import com.e.kotlinapp.ui.category.CategoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Developed by skydoves on 2018-01-20.
 * Copyright (c) 2018 skydoves rights reserved.
 */

@Suppress("unused")
@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModel::class)
    abstract fun bindCategoryViewModel(categoryViewModel: CategoryViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModelCoroutine::class)
    abstract fun bindCategoryViewModelCoroutine(categoryViewModelCoroutine: CategoryViewModelCoroutine): ViewModel

    @ExperimentalCoroutinesApi
    @Binds
    @IntoMap
    @ViewModelKey(CategoryViewModelFlow::class)
    abstract fun bindCategoryViewModelFlow(categoryViewModelFlow: CategoryViewModelFlow): ViewModel

    @Binds
    abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory): ViewModelProvider.Factory
}