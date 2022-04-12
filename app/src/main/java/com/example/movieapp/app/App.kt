package com.example.movieapp.app

import android.app.Application
import com.example.movieapp.di.component.AppComponent
import com.example.movieapp.di.component.DaggerAppComponent
import com.example.movieapp.di.module.ApplicationModule

class App : Application() {
    companion object {
        lateinit var instance: App
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.builder()
            .applicationModule(ApplicationModule(this)).build()
    }
}