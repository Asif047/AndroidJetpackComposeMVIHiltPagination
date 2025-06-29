package com.technonext.androidjetcakcomposemvihiltpagination.di

import com.google.gson.Gson
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.MovieDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi
import com.technonext.androidjetcakcomposemvihiltpagination.data.repository.MovieRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository
}