package com.example.movieapp.ui.viewstate

sealed class UIState<out T> {
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
    object Empty : UIState<Nothing>()
}