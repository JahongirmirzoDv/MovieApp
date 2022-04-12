package com.example.movieapp.di.module

import android.content.Context
import com.example.movieapp.app.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(var app: App) {
    @Provides
    @Singleton
    fun provideApp(): App = app


    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext
}