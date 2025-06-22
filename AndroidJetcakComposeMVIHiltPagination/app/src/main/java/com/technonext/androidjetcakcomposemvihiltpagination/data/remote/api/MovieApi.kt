package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api

import com.technonext.androidjetcakcomposemvihiltpagination.core.constants.ApiConstants
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {
    @GET(ApiConstants.POPULAR_MOVIES)
    suspend fun getPopularMovies(
        @Query(ApiConstants.QUERY_PAGE) page: Int = ApiConstants.DEFAULT_PAGE,
        @Query(ApiConstants.QUERY_LANGUAGE) language: String = ApiConstants.DEFAULT_LANGUAGE
    ): MoviesResponse
}