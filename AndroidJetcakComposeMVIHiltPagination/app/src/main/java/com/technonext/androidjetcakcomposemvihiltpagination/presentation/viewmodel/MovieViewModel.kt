package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetPopularMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.events.MovieEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.states.MovieState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MovieState())
    val state: StateFlow<MovieState> = _state.asStateFlow()

    val moviesPagingDataFlow: Flow<PagingData<Movie>> =
        getPopularMoviesUseCase()
            .cachedIn(viewModelScope)

    fun onAction(action: MovieEvent) {
        when (action) {
            is MovieEvent.LoadMovies -> {
                // Movies are automatically loaded through paging
            }

            is MovieEvent.Refresh -> {

            }

            is MovieEvent.OnMovieClick -> {
                // Handle movie click navigation
            }
        }
    }

    private fun updateState(update: (MovieState) -> MovieState) {
        _state.value = update(_state.value)
    }
}