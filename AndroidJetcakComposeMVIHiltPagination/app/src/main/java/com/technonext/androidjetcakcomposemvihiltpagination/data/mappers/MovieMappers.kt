package com.technonext.androidjetcakcomposemvihiltpagination.data.mappers

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.MovieDto
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie

fun MovieDto.toEntity(page: Int): MovieEntity {
    return MovieEntity(
        id = id,
        adult = adult,
        backdropPath = backdropPath,
        genreIds = try {
            Gson().toJson(genreIds)
        } catch (e: Exception) {
            "[]"
        },
        originalLanguage = originalLanguage,
        originalTitle = originalTitle,
        overview = overview,
        popularity = popularity,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        video = video,
        voteAverage = voteAverage,
        voteCount = voteCount,
        page = page
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = id,
        backdropPath = backdropPath,
        genreIds = Gson().fromJson(genreIds, object : TypeToken<List<Int>>() {}.type),
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        title = title,
        voteAverage = voteAverage,
        adult = adult,
        video = video,
        originalLanguage = originalLanguage,
        voteCount = voteCount,
        originalTitle = originalTitle,
        popularity = popularity
    )
}
