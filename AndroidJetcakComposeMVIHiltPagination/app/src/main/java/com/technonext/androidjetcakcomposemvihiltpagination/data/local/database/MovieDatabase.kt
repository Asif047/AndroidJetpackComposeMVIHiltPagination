package com.technonext.androidjetcakcomposemvihiltpagination.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.MovieDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}