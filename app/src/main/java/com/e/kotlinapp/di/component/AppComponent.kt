package com.e.kotlinapp.di.component

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.e.kotlinapp.App
import com.e.kotlinapp.MainActivity
import com.e.kotlinapp.di.AppModule
import com.e.kotlinapp.di.PersistenceModule
import com.e.kotlinapp.di.ViewModelModule
import com.e.kotlinapp.local.dao.CategoryDao
import com.e.kotlinapp.network.flow.CategoryViewModelFlow
import com.e.kotlinapp.ui.category.CategoryFragment
import com.e.kotlinapp.ui.category.CategoryFragmentCoroutine
import com.e.kotlinapp.ui.category.CategoryFragmentFlow
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AppModule::class, ViewModelModule::class, PersistenceModule::class]
)
interface AppComponent {
    fun inject(categoryFragment: App)

    fun inject(categoryFragmentFlow: CategoryFragmentFlow)
    fun inject(categoryFragment: CategoryFragment)
    fun inject(mainActivity: MainActivity)


    fun inject(categoryFragmentCoroutine: CategoryFragmentCoroutine)
    fun provideMovieDao(): CategoryDao
    fun bindViewModelFactory(): ViewModelProvider.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}