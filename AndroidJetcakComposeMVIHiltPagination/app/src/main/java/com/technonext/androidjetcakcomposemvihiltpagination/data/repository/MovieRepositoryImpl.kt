package com.technonext.androidjetcakcomposemvihiltpagination.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.mediator.MovieRemoteMediator
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Add these imports at the top of your Repository file:
import androidx.paging.ExperimentalPagingApi

import androidx.paging.map
import com.technonext.androidjetcakcomposemvihiltpagination.data.mappers.toDomain


// Fixed Repository Implementation:
@OptIn(ExperimentalPagingApi::class)
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : MovieRepository {

    override fun getPopularMoviesPaged(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            remoteMediator = MovieRemoteMediator(
                movieApi = movieApi,
                movieDatabase = movieDatabase
            ),
            pagingSourceFactory = {
                movieDatabase.movieDao().getAllMoviesPaging()
            }
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override fun getAllMovies(): Flow<List<Movie>> {
        return movieDatabase.movieDao().getAllMovies()
            .map { entities -> entities.map { entity -> entity.toDomain() } }
    }
}

// Extension function to convert entity to domain model
//private fun MovieEntity.toDomainModel(gson: Gson): Movie {
//    return Movie(
//        id = id,
//        title = title,
//        overview = overview,
//        posterPath = posterPath,
//        backdropPath = backdropPath,
//        voteAverage = voteAverage,
//        releaseDate = releaseDate,
//        genreIds = try {
//            gson.fromJson(genreIds, Array<Int>::class.java).toList()
//        } catch (e: Exception) {
//            emptyList()
//        }
//    )
//}