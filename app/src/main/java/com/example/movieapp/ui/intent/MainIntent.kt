package com.example.movieapp.ui.intent

sealed class MainIntent {
    object NowPlaying : MainIntent()
    object TopRated : MainIntent()
    object Upcoming : MainIntent()
    object Popular : MainIntent()
    object Recommendation : MainIntent()
    object Movie : MainIntent()
    object Videos : MainIntent()
    object Search : MainIntent()
}