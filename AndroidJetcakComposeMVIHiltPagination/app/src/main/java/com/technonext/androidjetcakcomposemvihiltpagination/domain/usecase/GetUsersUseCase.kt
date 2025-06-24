package com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): Result<List<User>> {
        return repository.getUsers()
    }
}