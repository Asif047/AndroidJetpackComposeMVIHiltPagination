package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces

import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiState
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface MovieViewModelInterface {
    val uiState: StateFlow<MovieUiState>
    val uiEvent: SharedFlow<MovieUiEvent>
    fun handleIntent(intent: MovieIntent)
}