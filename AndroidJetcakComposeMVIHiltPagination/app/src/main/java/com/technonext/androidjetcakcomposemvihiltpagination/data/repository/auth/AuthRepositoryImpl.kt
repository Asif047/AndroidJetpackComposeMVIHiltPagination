package com.technonext.androidjetcakcomposemvihiltpagination.data.repository.auth

import com.technonext.androidjetcakcomposemvihiltpagination.data.mappers.toDomain
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api.AuthApiServices
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Login
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.auth.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApiServices: AuthApiServices) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest): Result<Login> {
        return try {
            val response = authApiServices.login(loginRequest)
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}