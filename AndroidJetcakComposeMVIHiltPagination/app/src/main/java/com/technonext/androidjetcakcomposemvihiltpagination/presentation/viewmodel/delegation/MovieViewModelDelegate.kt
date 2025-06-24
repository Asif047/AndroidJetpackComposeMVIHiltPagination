package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation

import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MovieViewModelDelegate {
    val movieUiState: StateFlow<MovieUiState>
    val movieUiEvent: SharedFlow<MovieUiEvent>

    fun handleMovieIntent(intent: MovieIntent)
    fun loadMoreMovies()
    fun refreshMovies()
    fun clearMovieError()
}