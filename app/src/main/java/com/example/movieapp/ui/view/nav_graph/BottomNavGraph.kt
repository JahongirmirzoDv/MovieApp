package com.example.movieapp.ui.view.nav_graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.movieapp.ui.view.bottombar.BottomBarScreen
import com.example.movieapp.ui.view.screen.FeedScreen
import com.example.movieapp.ui.view.screen.SavedScreen
import com.example.movieapp.ui.view.screen.SettingsScreen
import com.example.movieapp.ui.view.utils.NetworkConnectionLiveData
import com.example.movieapp.ui.viewmodels.NetworkViewModel

@Composable
fun BottomNavGraph(
    navController: NavHostController,
    viewModel: NetworkViewModel,
    networkConnectionLiveData: NetworkConnectionLiveData,
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Feed.route
    ) {
        composable(route = BottomBarScreen.Feed.route) {
            FeedScreen(networkConnectionLiveData, viewModel)
        }
        composable(route = BottomBarScreen.Saved.route) {
            SavedScreen()
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen()
        }
    }
}