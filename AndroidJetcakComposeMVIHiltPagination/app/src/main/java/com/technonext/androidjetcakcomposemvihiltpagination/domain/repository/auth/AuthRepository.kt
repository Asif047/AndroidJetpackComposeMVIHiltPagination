package com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.auth

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Login

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Login>
}