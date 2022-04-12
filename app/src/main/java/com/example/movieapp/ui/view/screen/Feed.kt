@file:OptIn(ExperimentalPagerApi::class)

package com.example.movieapp.ui.view.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.paging.compose.collectAsLazyPagingItems
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieapp.ui.intent.MainIntent
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.view.utils.NetworkConnectionLiveData
import com.example.movieapp.ui.viewmodels.NetworkViewModel
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition",
    "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    networkConnectionLiveData: NetworkConnectionLiveData,
    viewModel: NetworkViewModel,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val lifecycle = LocalLifecycleOwner.current
    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) {
        ProvideWindowInsets(consumeWindowInsets = true, windowInsetsAnimationsEnabled = true) {
            MovieAppTheme {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    TopBar(icon = Icons.Default.Email, title = "Movie Plus")

                    networkConnectionLiveData.observe(lifecycle) {
                        if (it) {
                            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }

                    scope.launch(Dispatchers.Main) {
                        viewModel.channel.send(MainIntent.NowPlaying)
                    }
                    MovieCorousel(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TopBar(
    icon: ImageVector,
    title: String,
    iconsSize: Int = 30,
    iconsTint: Color = MaterialTheme.colors.onPrimary,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(iconsSize.dp),
            tint = iconsTint
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = title,
            fontSize = 28.sp,
            color = MaterialTheme.colors.onPrimary,
            fontWeight = FontWeight.W700
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.size(iconsSize.dp),
            tint = iconsTint
        )
    }
}


@OptIn(ExperimentalPagerApi::class, ExperimentalCoilApi::class)
@Composable
fun MovieCorousel(modifier: Modifier = Modifier, viewModel: NetworkViewModel) {
    val list = viewModel.movies.collectAsLazyPagingItems()
    val pagerState = rememberPagerState()

    HorizontalPager(
        count = list.itemCount,
        contentPadding = PaddingValues(horizontal = 44.dp, vertical = 5.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(500.dp),
        state = pagerState
    ) { page ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Card(
                Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(page).absoluteValue
                        lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        ).also { scale ->
                            scaleX = scale
                            scaleY = scale
                        }
                        alpha = lerp(
                            start = 0.85f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(35.dp))
                    .width(250.dp)
                    .height(450.dp)
            ) {
                Box(
                    Modifier.fillMaxSize()
                ) {
                    val painter = rememberImagePainter(
                        data = "https://image.tmdb.org/t/p/w500${list[page]?.poster_path}",
                        builder = {
                            crossfade(true)
                        }
                    )
                    val painterState = painter.state
                    Image(
                        painter = painter,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillWidth
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(25.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Text(
                            text = list[page]?.original_title ?: "empty",
                            color = Color.White,
                            style = MaterialTheme.typography.h1,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = list[page]?.release_date ?: "empty",
                            color = Color.White,
                            style = MaterialTheme.typography.h2
                        )
                    }
                }
            }
            Spacer(modifier = Modifier
                .height(15.dp)
                .fillMaxWidth())
            Text(
                text = list[page]?.original_title ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.W500,
            )
        }
    }
}


//lazyMovieItems.apply {
//    when {
//        loadState.refresh is LoadState.Loading -> {
//            item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
//        }
//        loadState.append is LoadState.Loading -> {
//            item { LoadingItem() }
//        }
//        loadState.refresh is LoadState.Error -> {
//            val e = lazyMovieItems.loadState.refresh as LoadState.Error
//            item {
//                ErrorItem(
//                    message = e.error.localizedMessage!!,
//                    modifier = Modifier.fillParentMaxSize(),
//                    onClickRetry = { retry() }
//                )
//            }
//        }
//        loadState.append is LoadState.Error -> {
//            val e = lazyMovieItems.loadState.append as LoadState.Error
//            item {
//                ErrorItem(
//                    message = e.error.localizedMessage!!,
//                    onClickRetry = { retry() }
//                )
//            }
//        }
//    }
//}