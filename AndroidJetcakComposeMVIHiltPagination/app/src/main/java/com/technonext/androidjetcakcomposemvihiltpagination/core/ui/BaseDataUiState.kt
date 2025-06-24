package com.technonext.androidjetcakcomposemvihiltpagination.core.ui

abstract class BaseDataUiState<T> {
    abstract val state: DataState<T>

    val isLoading: Boolean get() = state is DataState.Loading
    val isLoadingMore: Boolean get() = state is DataState.LoadingMore
    val error: String? get() = (state as? DataState.Error)?.message
    val hasMoreData: Boolean get() = (state as? DataState.Success)?.hasMoreData ?: true
}