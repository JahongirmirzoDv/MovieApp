package com.example.movieapp.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.api.ApiService
import com.example.movieapp.data.model.top_rated.Result
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalPagingApi
class TopRatedPaging @Inject constructor(
    val apiService: ApiService,
) : PagingSource<Int, Result>() {

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val nextPage = params.key ?: 1
            val userList = apiService.getTopRatedMovies(page = nextPage)
            LoadResult.Page(
                data = userList.results,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = if (userList.results.isEmpty()) null else userList.page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}