package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.data.remote.dto.LoginRequest
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.auth.LoginUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.auth.LoginEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.auth.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    // Channel for one-time navigation events
    private val _navigationEvent = Channel<LoginNavigationEvent>(Channel.BUFFERED)
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onAction(event: LoginEvent) {
        when (event) {
            is LoginEvent.Login -> {
                login(event.loginRequest)
            }
        }
    }

    private fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = loginUseCase(loginRequest)
            result.onSuccess { loginData ->
                _state.value = _state.value.copy(login = loginData, isLoading = false)
                // Send navigation event instead of relying on state
                _navigationEvent.trySend(LoginNavigationEvent.NavigateToUser)
            }.onFailure {
                _state.value = _state.value.copy(error = it.message, isLoading = false)
            }
        }
    }

    // Clear error method
    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }
}

// Keep your existing LoginEvent sealed class for user actions
// Add this for navigation events
sealed class LoginNavigationEvent {
    object NavigateToUser : LoginNavigationEvent()
}



