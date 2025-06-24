package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation

// 📄 presentation/viewmodel/delegate/UserViewModelDelegateImpl.kt

import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetUsersUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class UserViewModelDelegateImpl @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : UserViewModelDelegate {

    private val _userUiState = MutableStateFlow(UserUiState())
    override val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _userUiEvent = MutableSharedFlow<UserUiEvent>()
    override val userUiEvent: SharedFlow<UserUiEvent> = _userUiEvent.asSharedFlow()

    private lateinit var coroutineScope: CoroutineScope

    fun initialize(scope: CoroutineScope) {
        coroutineScope = scope
        handleUserIntent(UserIntent.LoadUsers)
    }

    override fun handleUserIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.RefreshUsers -> refreshUsers()
            is UserIntent.ClearError -> {

            }
        }
    }

    override fun refreshUsers() {
        coroutineScope.launch {
            _userUiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    override fun clearUserError() {
        _userUiState.update { it.clearError() }
    }

    private fun loadUsers() {
        if (_userUiState.value.isLoading) return

        coroutineScope.launch {
            _userUiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    private suspend fun loadUsersFromApi() {
        getUsersUseCase()
            .onSuccess { users ->
                _userUiState.update { it.toSuccess(users) }
            }
            .onFailure { error ->
                _userUiState.update { it.toError(error.message ?: "Unknown error") }
                _userUiEvent.emit(UserUiEvent.ShowError(error.message ?: "Unknown error"))
            }
    }
}