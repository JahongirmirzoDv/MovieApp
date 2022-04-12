package com.example.movieapp.data.model.upcoming

data class Upcoming(
    val dates: Dates,
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)