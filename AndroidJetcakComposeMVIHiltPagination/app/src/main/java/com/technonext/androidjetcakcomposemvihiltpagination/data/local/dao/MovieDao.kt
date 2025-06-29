package com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY page ASC, insertedAt ASC")
    fun getAllMoviesPaging(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies ORDER BY page ASC, insertedAt ASC")
    fun getAllMovies(): Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()

    @Query("SELECT COUNT(*) FROM movies")
    suspend fun getMovieCount(): Int
}
