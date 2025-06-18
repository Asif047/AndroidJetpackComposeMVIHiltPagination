package com.technonext.androidjetcakcomposemvihiltpagination.data.repository

import com.google.gson.Gson
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.MovieDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi,
    private val dao: MovieDao,
    private val gson: Gson
) : MovieRepository {
    override fun getMovies(): Flow<List<Movie>> {
        return dao.getAllMovies().map { entities ->
            entities.map { it.toDomainModel(gson) }
        }
    }
    override suspend fun loadMovies(page: Int): Result<Unit> {
        return try {
            val response = api.getPopularMovies(page = page)
            val entities = response.results.map { dto ->
                MovieEntity(
                    id = dto.id,
                    title = dto.title,
                    overview = dto.overview,
                    posterPath = dto.poster_path,
                    backdropPath = dto.backdrop_path,
                    voteAverage = dto.vote_average,
                    releaseDate = dto.release_date,
                    genreIds = gson.toJson(dto.genre_ids),
                    page = page
                )
            }
            dao.insertMovies(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getLastLoadedPage(): Int {
        return dao.getLastPage() ?: 0
    }
    override suspend fun clearMovies() {
        dao.clearAllMovies()
    }
}
// Extension function to convert entity to domain model
private fun MovieEntity.toDomainModel(gson: Gson): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        backdropPath = backdropPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate,
        genreIds = try {
            gson.fromJson(genreIds, Array<Int>::class.java).toList()
        } catch (e: Exception) {
            emptyList()
        }
    )
}