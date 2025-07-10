package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.auth.LoginUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.auth.LoginEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.auth.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onAction(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                login(event.loginRequest)
            }
        }
    }

    private fun login(loginRequest: com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = loginUseCase(loginRequest)
            result.onSuccess {
                _state.value = _state.value.copy(login = it, isLoading = false)
            }.onFailure {
                _state.value = _state.value.copy(error = it.message, isLoading = false)
            }
        }
    }
}