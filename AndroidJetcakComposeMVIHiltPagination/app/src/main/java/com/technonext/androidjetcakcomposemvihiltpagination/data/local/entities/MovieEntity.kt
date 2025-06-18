package com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val voteAverage: Double,
    val releaseDate: String,
    val genreIds: String, // JSON string of genre IDs
    val page: Int
)