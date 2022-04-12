package com.example.movieapp.data.model.recommendation

data class Recommendation(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)