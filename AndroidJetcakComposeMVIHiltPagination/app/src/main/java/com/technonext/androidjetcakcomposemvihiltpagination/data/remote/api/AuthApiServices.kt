package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiServices {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse
}