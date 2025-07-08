package com.technonext.androidjetcakcomposemvihiltpagination.data.mappers

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.UserDto
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        username = username,
        email = email
    )
}