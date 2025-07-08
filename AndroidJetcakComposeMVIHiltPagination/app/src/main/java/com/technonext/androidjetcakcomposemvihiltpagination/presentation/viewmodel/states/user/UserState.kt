package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.user

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User

data class UserState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)