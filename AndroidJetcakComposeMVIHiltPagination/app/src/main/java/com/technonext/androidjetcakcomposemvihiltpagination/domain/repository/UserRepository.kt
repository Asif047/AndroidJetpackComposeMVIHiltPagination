package com.technonext.androidjetcakcomposemvihiltpagination.domain.repository

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User

interface UserRepository {
    suspend fun getUsers(): Result<List<User>>
}

