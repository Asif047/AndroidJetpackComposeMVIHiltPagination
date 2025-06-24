package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces

import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface UserViewModelInterface {
    val uiState: StateFlow<UserUiState>
    val uiEvent: SharedFlow<UserUiEvent>
    fun handleIntent(intent: UserIntent)
}