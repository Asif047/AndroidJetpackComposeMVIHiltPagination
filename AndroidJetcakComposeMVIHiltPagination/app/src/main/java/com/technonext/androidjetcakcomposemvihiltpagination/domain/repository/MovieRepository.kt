package com.technonext.androidjetcakcomposemvihiltpagination.domain.repository

import androidx.paging.PagingData
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getPopularMoviesPaged(): Flow<PagingData<Movie>>
    fun getAllMovies(): Flow<List<Movie>>
}