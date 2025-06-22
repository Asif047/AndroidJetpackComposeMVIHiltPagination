package com.technonext.androidjetcakcomposemvihiltpagination.core.constants

object ApiConstants {
    const val BASE_URL = "https://api.themoviedb.org/3/"

    // Endpoints
    const val POPULAR_MOVIES = "movie/popular"
    const val TOP_RATED_MOVIES = "movie/top_rated"
    const val MOVIE_DETAILS = "movie/{movie_id}"

    // Query Parameters
    const val QUERY_API_KEY = "api_key"
    const val QUERY_PAGE = "page"
    const val QUERY_LANGUAGE = "language"

    // Default Values
    const val DEFAULT_PAGE = 1
    const val DEFAULT_LANGUAGE = "en-US"
}