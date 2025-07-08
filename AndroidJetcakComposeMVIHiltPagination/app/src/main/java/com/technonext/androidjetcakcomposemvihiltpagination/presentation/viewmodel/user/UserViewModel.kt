package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.user.GetUsersUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.user.UserEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.user.UserState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val getUsersUseCase: GetUsersUseCase) : ViewModel() {
    private val _state = MutableStateFlow(UserState())
    val state: StateFlow<UserState> = _state.asStateFlow()

    init {
        onAction(UserEvent.LoadUsers)
    }

    fun onAction(event: UserEvent) {
        when (event) {
            is UserEvent.LoadUsers -> {
                loadUsers()
            }
        }
    }

    private fun loadUsers() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val result = getUsersUseCase()
            result.onSuccess {
                _state.value = _state.value.copy(users = it, isLoading = false)
            }.onFailure {
                _state.value = _state.value.copy(error = it.message, isLoading = false)
            }
        }
    }
}