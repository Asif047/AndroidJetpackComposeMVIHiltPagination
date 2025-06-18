package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto

data class MovieDto(
    val id: Int,
    val title: String,
    val overview: String,
    val poster_path: String?,
    val backdrop_path: String?,
    val vote_average: Double,
    val release_date: String,
    val genre_ids: List<Int>
)