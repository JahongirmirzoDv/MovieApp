package com.example.movieapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.app.App
import com.example.movieapp.data.api.ApiService
import com.example.movieapp.ui.view.bottombar.BottomBar
import com.example.movieapp.ui.view.bottombar.BottomBarScreen
import com.example.movieapp.ui.view.model.bottombar.BottomBarItem
import com.example.movieapp.ui.view.nav_graph.BottomNavGraph
import com.example.movieapp.ui.view.utils.NetworkConnectionLiveData
import com.example.movieapp.ui.viewmodels.NetworkViewModel
import com.google.accompanist.insets.ui.Scaffold
import javax.inject.Inject
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    lateinit var viewModel: NetworkViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject(this)
        viewModel = NetworkViewModel(apiService)

        setContent {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            val bottomBarHeight = 75.dp
            val bottomBarHeightPx = with(LocalDensity.current) {
                bottomBarHeight.roundToPx().toFloat()
            }
            val bottomBarOffsetHeightPx = remember { mutableStateOf(0f) }
            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(
                        available: Offset,
                        source: NestedScrollSource,
                    ): Offset {
                        val delta = available.y
                        val newOffset = bottomBarOffsetHeightPx.value + delta
                        bottomBarOffsetHeightPx.value =
                            newOffset.coerceIn(-bottomBarHeightPx, 0f)
                        return Offset.Zero
                    }
                }
            }

            val navController = rememberNavController()
            val scaffoldState = rememberScaffoldState()

            Scaffold(
                bottomBar = {
                    BottomBar(
                        list = listOf(
                            BottomBarItem(
                                "Feed",
                                BottomBarScreen.Feed.route,
                                Icons.Default.Home,
                            ),
                            BottomBarItem(
                                "Saved",
                                BottomBarScreen.Saved.route,
                                Icons.Default.Favorite,
                            ),
                            BottomBarItem(
                                "Settings",
                                BottomBarScreen.Settings.route,
                                Icons.Default.Settings,
                            ),
                        ),
                        navController = navController,
                        onClick = {
                            navController.navigate(it.route)
                        },
                        modifier = Modifier
                            .height( bottomBarHeight)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = -bottomBarOffsetHeightPx.value.roundToInt())
                            }
                    )
                },
                scaffoldState = scaffoldState,
                modifier = Modifier.nestedScroll(nestedScrollConnection)
            ) {
                BottomNavGraph(navController = navController, viewModel, networkConnectionLiveData)
            }
        }
    }
}
