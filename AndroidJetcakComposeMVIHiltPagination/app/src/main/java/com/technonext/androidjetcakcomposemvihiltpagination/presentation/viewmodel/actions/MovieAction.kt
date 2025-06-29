package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.actions

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie

sealed class MovieAction {
    data object LoadMovies : MovieAction()
    data object Refresh : MovieAction()
    data class OnMovieClick(val movie: Movie) : MovieAction()
}