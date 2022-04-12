package com.example.movieapp.ui.view.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.accompanist.insets.ProvideWindowInsets

@Composable
fun SavedScreen() {
    ProvideWindowInsets {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Green),
        ) {

        }
    }
}