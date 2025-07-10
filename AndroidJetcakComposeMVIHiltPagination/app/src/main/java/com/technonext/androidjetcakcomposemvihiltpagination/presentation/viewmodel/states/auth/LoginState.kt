package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.auth

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Login

data class LoginState(
    val isLoading: Boolean = false,
    val login: Login? = null,
    val error: String? = null
)