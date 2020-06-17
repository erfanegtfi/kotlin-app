package com.e.kotlinapp.di

import android.app.Application
import androidx.annotation.NonNull
import com.e.kotlinapp.local.IkoponDatabase
import com.e.kotlinapp.local.dao.CategoryDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {


    @Provides
    @Singleton
    fun provideMovieDao(@NonNull database: IkoponDatabase): CategoryDao {
        return database.getCategoryDao()
    }

}