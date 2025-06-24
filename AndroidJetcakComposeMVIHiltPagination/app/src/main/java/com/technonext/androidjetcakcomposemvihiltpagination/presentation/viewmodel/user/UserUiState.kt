package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user

// 📄 presentation/viewmodel/UserUiState.kt

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.User
import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.BaseDataUiState
import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.DataState

data class UserUiState(
    override val state: DataState<List<User>> = DataState.Idle
) : BaseDataUiState<List<User>>() {

    // Convenience property to get users
    val users: List<User> get() = (state as? DataState.Success)?.data ?: emptyList()

    // State transition methods
    fun toLoading() = copy(state = DataState.Loading)

    fun toSuccess(users: List<User>) =
        copy(state = DataState.Success(users, hasMoreData = false)) // No pagination for users

    fun toError(message: String) =
        copy(state = DataState.Error(message, wasLoadingMore = false))

    fun clearError() = when (state) {
        is DataState.Error -> {
            if (users.isNotEmpty()) {
                copy(state = DataState.Success(users, hasMoreData = false))
            } else {
                copy(state = DataState.Idle)
            }
        }
        else -> this
    }
}

// Intent and Event classes for Users
sealed class UserIntent {
    object LoadUsers : UserIntent()
    object RefreshUsers : UserIntent()
    object ClearError : UserIntent()
}

sealed class UserUiEvent {
    data class ShowError(val message: String) : UserUiEvent()
}