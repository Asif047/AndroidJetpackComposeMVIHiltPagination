package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto

data class MoviesResponse(
    val page: Int,
    val results: List<MovieDto>,
    val total_pages: Int,
    val total_results: Int
)