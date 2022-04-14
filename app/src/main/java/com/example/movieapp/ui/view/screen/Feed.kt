@file:OptIn(ExperimentalPagerApi::class)

package com.example.movieapp.ui.view.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.movieapp.R
import com.example.movieapp.ui.intent.MainIntent
import com.example.movieapp.ui.theme.MovieAppTheme
import com.example.movieapp.ui.view.screen.movie.MovieDetail
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
import kotlin.math.roundToInt

@SuppressLint("StateFlowValueCalledInComposition", "CoroutineCreationDuringComposition",
    "UnusedMaterialScaffoldPaddingParameter")
@Composable
fun FeedScreen(
    networkConnectionLiveData: NetworkConnectionLiveData,
    viewModel: NetworkViewModel,
    navController: NavController,
) {
    val scope = rememberCoroutineScope()
    scope.launch(Dispatchers.Main) {
        viewModel.channel.send(MainIntent.NowPlaying)
        viewModel.channel.send(MainIntent.TopRated)
    }

    Scaffold(
        modifier = Modifier.systemBarsPadding()
    ) {
        ProvideWindowInsets(consumeWindowInsets = true, windowInsetsAnimationsEnabled = true) {
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
                        TopBar(icon = Icons.Default.Email, title = "Movie Plus", modifier = Modifier
                            .height(toolbarHeight)
                            .offset {
                                IntOffset(
                                    x = 0,
                                    y = toolbarOffsetHeightPx.value.roundToInt()
                                )
                            })
                        MovieCorousel(viewModel = viewModel, navController = navController)
                        Spacer(modifier = Modifier.height(15.dp))
                        Text(text = "Top Rated",
                            fontSize = 25.sp,
                            color = MaterialTheme.colors.onPrimary,
                            fontWeight = FontWeight.W500,
                            modifier = Modifier.padding(start = 20.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalFilms(viewModel = viewModel)
                    }
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
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 20.dp, end = 20.dp),
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
            color = iconsTint,
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
fun MovieCorousel(
    modifier: Modifier = Modifier,
    viewModel: NetworkViewModel,
    navController: NavController,
) {
    val list = viewModel.movies.collectAsLazyPagingItems()
    val pagerState = rememberPagerState()
    val context = LocalContext.current

    Text(
        text = "Top Movies",
        fontSize = 25.sp,
        color = MaterialTheme.colors.onPrimary,
        fontWeight = FontWeight.W500,
        modifier = Modifier
            .padding(start = 20.dp)
    )
    Spacer(modifier = Modifier.height(15.dp))
    HorizontalPager(
        count = list.itemCount,
        contentPadding = PaddingValues(horizontal = 64.dp, vertical = 5.dp),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
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
                            start = 0.65f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                    .width(250.dp)
                    .clip(RoundedCornerShape(35.dp))
                    .wrapContentHeight()
                    .clickable {
                        val intent = Intent(context, MovieDetail::class.java)
                        intent.putExtra(list[page]?.id.toString(), "id")
                        context.startActivity(intent)
                    }
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(370.dp)
                ) {
                    val painter = rememberImagePainter(
                        data = "https://image.tmdb.org/t/p/w500${list[page]?.poster_path}",
                        builder = {
                            crossfade(true)
                        }
                    )
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
                maxLines = 2,
                fontWeight = FontWeight.W500,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 10.dp)
            )
        }

    }
}

@Composable
fun HorizontalFilms(viewModel: NetworkViewModel) {
    val context = LocalContext.current
    val list = viewModel.top_rated.collectAsLazyPagingItems()
    LazyRow(modifier = Modifier.padding(bottom = 20.dp)) {
        items(items = list) { item ->
            TopRatedItem(image = item!!.poster_path,
                title = item.original_title ?: "",
                modifier = Modifier.clickable {
                    val intent = Intent(context, MovieDetail::class.java)
                    intent.putExtra(item.id.toString(), "id")
                    context.startActivity(intent)
                })
        }
    }
}

@Composable
fun TopRatedItem(
    image: String,
    title: String,
    modifier: Modifier,
) {
    val painter = rememberImagePainter(
        data = "https://image.tmdb.org/t/p/w500$image",
        builder = {
            crossfade(true)
            error(R.drawable.no_image)
        }
    )
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .padding(start = 30.dp)
            .width(155.dp)
    ) {
        Card(
            modifier = Modifier
                .width(150.dp)
                .height(200.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Green)
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.body2,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}