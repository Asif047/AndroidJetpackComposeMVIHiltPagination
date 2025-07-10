package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.auth

import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest

sealed class LoginEvent {
    data class Login(val loginRequest: LoginRequest) : LoginEvent()
}