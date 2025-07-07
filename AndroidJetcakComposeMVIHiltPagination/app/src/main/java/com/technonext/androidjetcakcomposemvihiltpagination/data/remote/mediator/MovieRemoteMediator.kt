package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.RemoteKeys
import com.technonext.androidjetcakcomposemvihiltpagination.data.mappers.toEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi

@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator(
    private val movieApi: MovieApi,
    private val movieDatabase: MovieDatabase
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: 1
                }

                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                    prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }

                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                    nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                }
            }

            val apiResponse = movieApi.getPopularMovies(
                language = "en-US",
                page = page
            )

            val movies = apiResponse.results
            val endOfPaginationReached = movies.isEmpty() || page >= apiResponse.totalPages

            movieDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    movieDatabase.remoteKeysDao().clearRemoteKeys()
                    movieDatabase.movieDao().clearAllMovies()
                }

                val prevKey = if (page > 1) page - 1 else null
                val nextKey = if (endOfPaginationReached) null else page + 1

                val remoteKeys = movies.map {
                    RemoteKeys(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                movieDatabase.remoteKeysDao().insertAll(remoteKeys)

                val movieEntities = movies.map { movieDto ->
                    movieDto.toEntity(page)
                }
                movieDatabase.movieDao().insertMovies(movieEntities)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, MovieEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                movieDatabase.remoteKeysDao().getRemoteKeysId(movieId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, MovieEntity>
    ): RemoteKeys? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            movieDatabase.remoteKeysDao().getRemoteKeysId(movie.id)
        }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, MovieEntity>
    ): RemoteKeys? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            movieDatabase.remoteKeysDao().getRemoteKeysId(movie.id)
        }
    }
}