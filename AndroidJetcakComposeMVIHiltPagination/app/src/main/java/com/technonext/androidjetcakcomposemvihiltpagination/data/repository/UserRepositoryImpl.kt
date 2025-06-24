package com.technonext.androidjetcakcomposemvihiltpagination.data.repository

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.UserApi
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.toDomainModel
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val response = api.getUsers()
            val users = response.map { it.toDomainModel() }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}