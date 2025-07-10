package com.technonext.androidjetcakcomposemvihiltpagination.di

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.AuthApiServices
import com.technonext.androidjetcakcomposemvihiltpagination.data.repository.auth.AuthRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.auth.AuthRepository
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.ApiServices
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
    fun provideUserRepository(apiServices: ApiServices): UserRepository {
        return UserRepositoryImpl(apiServices)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(authApiServices: AuthApiServices): AuthRepository {
        return AuthRepositoryImpl(authApiServices)
    }

    @Provides
    @Singleton
    fun provideMovieRepository(apiServices: ApiServices, movieDatabase: MovieDatabase): MovieRepository {
        return MovieRepositoryImpl(apiServices, movieDatabase)
    }
}