package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel

import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.BaseDataUiState
import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.DataState
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie

data class MovieUiState(
    override val state: DataState<List<Movie>> = DataState.Idle
) : BaseDataUiState<List<Movie>>() {

    // Convenience property to get movies
    val movies: List<Movie> get() = (state as? DataState.Success)?.data ?: emptyList()

    // State transition methods
    fun toLoading() = copy(state = DataState.Loading)

    fun toLoadingMore() = copy(state = DataState.LoadingMore)

    fun toSuccess(movies: List<Movie>, hasMore: Boolean = true) =
        copy(state = DataState.Success(movies, hasMore))

    fun toError(message: String, wasLoadingMore: Boolean = false) =
        copy(state = DataState.Error(message, wasLoadingMore))

    fun clearError() = when (state) {
        is DataState.Error -> {
            if (movies.isNotEmpty()) {
                copy(state = DataState.Success(movies, hasMoreData))
            } else {
                copy(state = DataState.Idle)
            }
        }
        else -> this
    }
}

// Intent and Event classes remain the same
sealed class MovieIntent {
    object LoadMovies : MovieIntent()
    object LoadMoreMovies : MovieIntent()
    object RefreshMovies : MovieIntent()
    object ClearError : MovieIntent()
}

sealed class MovieUiEvent {
    data class ShowError(val message: String) : MovieUiEvent()
}