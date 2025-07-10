package com.technonext.androidjetcakcomposemvihiltpagination.data.mappers

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginResponse
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Login

fun LoginResponse.toDomain(): Login {
    return Login(
        accessToken = accessToken,
        refreshToken = refreshToken,
        id = id,
        username = username,
        email = email,
        firstName = firstName,
        lastName = lastName,
        gender = gender,
        image = image
    )
}