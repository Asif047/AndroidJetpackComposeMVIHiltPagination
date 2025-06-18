package com.technonext.androidjetcakcomposemvihiltpagination.domain.repository

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<List<Movie>>
    suspend fun loadMovies(page: Int): Result<Unit>
    suspend fun getLastLoadedPage(): Int
    suspend fun clearMovies()
}