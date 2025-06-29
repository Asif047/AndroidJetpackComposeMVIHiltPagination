//package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation
//
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.flow.StateFlow
//
//interface UserViewModelDelegate {
//    val userUiState: StateFlow<UserUiState>
//    val userUiEvent: SharedFlow<UserUiEvent>
//
//    fun handleUserIntent(intent: UserIntent)
//    fun refreshUsers()
//    fun clearUserError()
//}