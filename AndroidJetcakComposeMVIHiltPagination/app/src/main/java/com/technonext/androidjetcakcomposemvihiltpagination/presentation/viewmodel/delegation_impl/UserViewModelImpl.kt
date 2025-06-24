package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation_impl

// 📄 presentation/viewmodel/UserViewModelImpl.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetUsersUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces.UserViewModelInterface
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModelImpl @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel(), UserViewModelInterface {

    private val _uiState = MutableStateFlow(UserUiState())
    override val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UserUiEvent>()
    override val uiEvent: SharedFlow<UserUiEvent> = _uiEvent.asSharedFlow()

    init {
        handleIntent(UserIntent.LoadUsers)
    }

    override fun handleIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.RefreshUsers -> refreshUsers()
            is UserIntent.ClearError -> clearError()
        }
    }

    private fun loadUsers() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    private fun refreshUsers() {
        viewModelScope.launch {
            _uiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    private suspend fun loadUsersFromApi() {
        getUsersUseCase()
            .onSuccess { users ->
                _uiState.update { it.toSuccess(users) }
            }
            .onFailure { error ->
                _uiState.update { it.toError(error.message ?: "Unknown error") }
                _uiEvent.emit(UserUiEvent.ShowError(error.message ?: "Unknown error"))
            }
    }

    private fun clearError() {
        _uiState.update { it.clearError() }
    }
}