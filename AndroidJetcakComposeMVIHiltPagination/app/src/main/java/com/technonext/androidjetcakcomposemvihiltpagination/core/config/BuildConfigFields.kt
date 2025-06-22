package com.technonext.androidjetcakcomposemvihiltpagination.core.config

import com.asif047.androidjetpackcomposemvihiltpagination.BuildConfig


object BuildConfigFields {
    val API_KEY: String get() = BuildConfig.TMDB_API_KEY
    val BASE_URL: String get() = BuildConfig.BASE_URL
    val IS_DEBUG: Boolean get() = BuildConfig.DEBUG
}