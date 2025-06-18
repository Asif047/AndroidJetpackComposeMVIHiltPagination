package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.MoviesResponse
import retrofit2.http.GET
import retrofit2.http.Query
interface MovieApi {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "8f3845576311ddddc2ae7f7801641fdb", // Replace with your TMDB API k
        @Query("page") page: Int = 1
    ): MoviesResponse
}