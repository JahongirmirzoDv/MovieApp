package com.example.movieapp.di.component

import com.example.movieapp.MainActivity
import com.example.movieapp.di.module.ApplicationModule
import com.example.movieapp.di.module.NetworkModule
import com.example.movieapp.ui.view.screen.movie.MovieDetail
import dagger.Component
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class,NetworkModule::class])
@Singleton
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject2(movieDetail: MovieDetail)
}