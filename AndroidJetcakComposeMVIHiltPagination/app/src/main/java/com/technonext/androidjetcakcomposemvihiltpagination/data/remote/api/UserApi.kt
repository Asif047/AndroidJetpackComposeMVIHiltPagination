package com.technonext.androidjetcakcomposemvihiltpagination.data.remote.api

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.UserDto
import retrofit2.http.GET

interface UserApi {
    @GET("users")
    suspend fun getUsers(): List<UserDto>
}