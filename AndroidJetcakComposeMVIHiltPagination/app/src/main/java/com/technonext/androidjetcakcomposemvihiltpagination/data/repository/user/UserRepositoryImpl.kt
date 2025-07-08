package com.technonext.androidjetcakcomposemvihiltpagination.data.repository.user

import com.technonext.androidjetcakcomposemvihiltpagination.data.mappers.toDomain
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.MovieApi
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.user.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val movieApi: MovieApi) : UserRepository {
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = movieApi.getUsers()
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}