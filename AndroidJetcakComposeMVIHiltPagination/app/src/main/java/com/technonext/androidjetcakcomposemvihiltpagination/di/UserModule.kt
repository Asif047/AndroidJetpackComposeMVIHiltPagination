package com.technonext.androidjetcakcomposemvihiltpagination.di

// 📄 di/UserModule.kt

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.UserApi
import com.technonext.androidjetcakcomposemvihiltpagination.data.repository.UserRepositoryImpl
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserRetrofit

@Module
@InstallIn(SingletonComponent::class)
abstract class UserModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    companion object {
        @Provides
        @Singleton
        @UserRetrofit
        fun provideUserRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @Singleton
        fun provideUserApi(@UserRetrofit retrofit: Retrofit): UserApi {
            return retrofit.create(UserApi::class.java)
        }
    }
}