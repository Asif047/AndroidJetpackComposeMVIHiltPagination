package com.technonext.androidjetcakcomposemvihiltpagination.core.ui

sealed interface DataState<out T> {
    data object Idle : DataState<Nothing>
    data object Loading : DataState<Nothing>
    data object LoadingMore : DataState<Nothing>
    data class Success<T>(val data: T, val hasMoreData: Boolean = true) : DataState<T>
    data class Error(val message: String, val wasLoadingMore: Boolean = false) : DataState<Nothing>
}