package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states

data class MovieState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null
)