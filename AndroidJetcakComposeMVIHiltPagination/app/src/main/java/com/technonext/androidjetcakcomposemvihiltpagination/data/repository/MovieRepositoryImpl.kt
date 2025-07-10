package com.technonext.androidjetcakcomposemvihiltpagination.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase
import com.technonext.androidjetcakcomposemvihiltpagination.data.mappers.toDomain
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.ApiServices
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.mediator.MovieRemoteMediator
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class MovieRepositoryImpl @Inject constructor(
    private val apiServices: ApiServices,
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
                apiServices = apiServices,
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
