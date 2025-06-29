package com.technonext.androidjetcakcomposemvihiltpagination.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.MovieDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.dao.RemoteKeysDao
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.MovieEntity
import com.technonext.androidjetcakcomposemvihiltpagination.data.local.entities.RemoteKeys

@Database(
    entities = [MovieEntity::class, RemoteKeys::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}