package com.technonext.androidjetcakcomposemvihiltpagination.di

import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi
import com.technonext.androidjetcakcomposemvihiltpagination.data.repository.MovieRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.data.repository.user.UserRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.user.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(movieApi: MovieApi): UserRepository {
        return UserRepositoryImpl(movieApi)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(movieApi: MovieApi, movieDatabase: MovieDatabase): MovieRepository {
        return MovieRepositoryImpl(movieApi, movieDatabase)
    }
}