package com.example.movieapp.data.api

import com.example.movieapp.data.model.credits.Credits
import com.example.movieapp.data.model.movie.Movie
import com.example.movieapp.data.model.now_playing.NowPlaying
import com.example.movieapp.data.model.popular.Popular
import com.example.movieapp.data.model.recommendation.Recommendation
import com.example.movieapp.data.model.search.Search
import com.example.movieapp.data.model.top_rated.TopRated
import com.example.movieapp.data.model.upcoming.Upcoming
import com.example.movieapp.data.model.video.Videos
import com.example.movieapp.ui.view.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface ApiService {

    @GET("movie/now_playing")
    suspend fun getNowPlayingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int
    ):NowPlaying

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ):TopRated

    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ):Upcoming

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = Constants.API_KEY,
        @Query("page") page: Int,
    ):Popular

    @GET("movie/{id}/recommendations")
    suspend fun getRecommendations(
        @Path("id") movieId: String,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ):Recommendation

    @GET("movie/{id}/credits")
    suspend fun getCredits(
        @Path("id") movieId: String?,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ):Credits

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: String?,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ):Movie

    @GET("movie/{id}/videos")
    suspend fun getMovieVideos(
        @Path("id") movieId: String?,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ):Videos

    @GET("search/movie")
    suspend fun getSearchMovies(
        @Query("query") query: String?,
        @Query("api_key") apiKey: String = Constants.API_KEY,
    ):Search
}