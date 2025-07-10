package com.technonext.androidjetcakcomposemvihiltpagination.domain.model

data class Login(
    val accessToken: String,
    val refreshToken: String,
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val image: String
)