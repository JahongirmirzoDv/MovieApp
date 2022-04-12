package com.example.movieapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.data.api.ApiService
import com.example.movieapp.data.model.now_playing.Result
import com.example.movieapp.data.paging.NowPlayingPaging
import com.example.movieapp.ui.intent.MainIntent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class NetworkViewModel @Inject constructor(
    val apiService: ApiService,
) : ViewModel() {
    val channel = Channel<MainIntent>(Channel.UNLIMITED)

    @OptIn(ExperimentalPagingApi::class)
    val movies: Flow<PagingData<Result>> = Pager(PagingConfig(pageSize = 20)) {
        NowPlayingPaging(apiService)
    }.flow

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.NowPlaying -> movies
                    else -> {}
                }
            }
        }
    }
}