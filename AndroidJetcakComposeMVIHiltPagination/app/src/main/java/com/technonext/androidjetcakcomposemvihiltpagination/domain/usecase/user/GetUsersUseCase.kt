package com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.user

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(): Result<List<User>> {
        return userRepository.getUsers()
    }
}