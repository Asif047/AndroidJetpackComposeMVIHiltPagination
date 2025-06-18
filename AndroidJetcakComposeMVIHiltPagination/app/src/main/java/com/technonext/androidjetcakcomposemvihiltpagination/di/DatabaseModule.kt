package com.technonext.androidjetcakcomposemvihiltpagination.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import androidx.room.Room
import javax.inject.Singleton
import android.content.Context
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.MovieDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.database.MovieDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            "movie_database"
        ).build()
    }
    @Provides
    fun provideMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }
}