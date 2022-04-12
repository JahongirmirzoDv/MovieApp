package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import com.example.movieapp.app.App
import com.example.movieapp.data.api.ApiService
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.view.MainScreen
import com.example.movieapp.ui.view.utils.NetworkConnectionLiveData
import com.example.movieapp.ui.viewmodels.NetworkViewModel
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var apiService: ApiService
    @Inject
    lateinit var networkConnectionLiveData: NetworkConnectionLiveData

    lateinit var viewModel: NetworkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel = NetworkViewModel(apiService)
        setContent {
            MovieAppTheme {
                MainScreen(viewModel,networkConnectionLiveData)
            }
        }
    }
}
