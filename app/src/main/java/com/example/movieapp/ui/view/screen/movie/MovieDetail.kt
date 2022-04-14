package com.example.movieapp.ui.view.screen.movie

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.app.App
import com.example.movieapp.data.api.ApiService
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.view.screen.TopBar
import com.example.movieapp.ui.view.utils.NetworkConnectionLiveData
import com.example.movieapp.ui.viewmodels.NetworkViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.ui.Scaffold
import javax.inject.Inject
import kotlin.math.roundToInt

class MovieDetail : ComponentActivity() {
    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var networkConnectionLiveData: NetworkConnectionLiveData
    lateinit var viewModel: NetworkViewModel

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.appComponent.inject2(this)
        viewModel = NetworkViewModel(apiService)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val movie_id = intent.getStringExtra("id")
        setContent {
            Scaffold(
                modifier = Modifier.systemBarsPadding()
            ) {
                val navController = rememberNavController()
                ProvideWindowInsets(consumeWindowInsets = true,
                    windowInsetsAnimationsEnabled = true) {
                    MovieAppTheme {
                        val toolbarHeight = 75.dp
                        val toolbarHeightPx =
                            with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
                        val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }
                        val nestedScrollConnection = remember {
                            object : NestedScrollConnection {
                                override fun onPreScroll(
                                    available: Offset,
                                    source: NestedScrollSource,
                                ): Offset {
                                    val delta = available.y
                                    val newOffset = toolbarOffsetHeightPx.value + delta
                                    toolbarOffsetHeightPx.value =
                                        newOffset.coerceIn(-toolbarHeightPx, 0f)
                                    return Offset.Zero
                                }
                            }
                        }
                        val lazyListState = rememberLazyListState()

                        Scaffold(
                            modifier = Modifier
                                .nestedScroll(nestedScrollConnection)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                            ) {
                                TopBar(icon = Icons.Default.Email,
                                    title = "Movie Plus",
                                    modifier = Modifier
                                        .height(toolbarHeight)
                                        .offset {
                                            IntOffset(
                                                x = 0,
                                                y = toolbarOffsetHeightPx.value.roundToInt()
                                            )
                                        })







                            }
                        }
                    }
                }
            }
        }
    }
}