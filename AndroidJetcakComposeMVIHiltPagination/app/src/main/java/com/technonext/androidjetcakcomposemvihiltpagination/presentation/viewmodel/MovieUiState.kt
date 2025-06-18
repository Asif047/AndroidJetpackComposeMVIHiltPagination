package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie

data class MovieUiState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isLoadingMore: Boolean = false,
    val hasMoreData: Boolean = true
)
sealed class MovieIntent {
    object LoadMovies : MovieIntent()
    object LoadMoreMovies : MovieIntent()
    object RefreshMovies : MovieIntent()
    object ClearError : MovieIntent()
}
sealed class MovieUiEvent {
    data class ShowError(val message: String) : MovieUiEvent()
}