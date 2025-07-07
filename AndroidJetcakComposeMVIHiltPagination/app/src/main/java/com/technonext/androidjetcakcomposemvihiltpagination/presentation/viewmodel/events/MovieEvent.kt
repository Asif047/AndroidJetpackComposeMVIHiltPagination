package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie

sealed class MovieEvent {
    data object LoadMovies : MovieEvent()
    data object Refresh : MovieEvent()
    data class OnMovieClick(val movie: Movie) : MovieEvent()
}