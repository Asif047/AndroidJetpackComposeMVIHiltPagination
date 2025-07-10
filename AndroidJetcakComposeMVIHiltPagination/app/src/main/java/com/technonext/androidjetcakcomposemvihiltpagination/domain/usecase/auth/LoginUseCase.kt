package com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.auth

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Login
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.auth.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest): Result<Login> {
        return authRepository.login(loginRequest)
    }
}