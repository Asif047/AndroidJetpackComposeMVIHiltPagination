package com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao

import androidx.room.*
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY page, id")
    fun getAllMovies(): Flow<List<MovieEntity>>
    @Query("SELECT * FROM movies WHERE page = :page")
    suspend fun getMoviesByPage(page: Int): List<MovieEntity>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
    @Query("SELECT MAX(page) FROM movies")
    suspend fun getLastPage(): Int?
}