package com.e.kotlinapp

import android.app.Application
import com.e.kotlinapp.di.AppModule
import com.e.kotlinapp.di.component.AppComponent
import com.e.kotlinapp.di.component.DaggerAppComponent

class App : Application() {

    lateinit var component: AppComponent

    override fun onCreate() {
        super.onCreate()

        createComponent()
        component.inject(this)
    }

    fun createComponent() {
        component = DaggerAppComponent.factory()
            .create(this);
    }
}