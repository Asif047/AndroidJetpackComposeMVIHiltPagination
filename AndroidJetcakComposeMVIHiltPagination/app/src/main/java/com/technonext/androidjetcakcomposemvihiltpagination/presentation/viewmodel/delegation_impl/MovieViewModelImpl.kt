package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation_impl

// 📄 presentation/viewmodel/MovieViewModelImpl.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.DataState
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.LoadMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiState
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.interfaces.MovieViewModelInterface
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModelImpl @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val repository: MovieRepository
) : ViewModel(), MovieViewModelInterface {

    private val _uiState = MutableStateFlow(MovieUiState())
    override val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MovieUiEvent>()
    override val uiEvent: SharedFlow<MovieUiEvent> = _uiEvent.asSharedFlow()

    private var currentPage = 1
    private var isLastPage = false

    init {
        observeMovies()
        handleIntent(MovieIntent.LoadMovies)
    }

    override fun handleIntent(intent: MovieIntent) {
        when (intent) {
            is MovieIntent.LoadMovies -> loadMovies()
            is MovieIntent.LoadMoreMovies -> loadMoreMovies()
            is MovieIntent.RefreshMovies -> refreshMovies()
            is MovieIntent.ClearError -> clearError()
        }
    }

    private fun observeMovies() {
        getMoviesUseCase()
            .onEach { movies ->
                // Only update if we're in a success state or have no state yet
                val currentState = _uiState.value.state
                when (currentState) {
                    is DataState.Success -> {
                        _uiState.update { it.toSuccess(movies, currentState.hasMoreData) }
                    }
                    is DataState.Idle -> {
                        if (movies.isNotEmpty()) {
                            _uiState.update { it.toSuccess(movies) }
                        }
                    }
                    // Don't update during loading states to preserve UI feedback
                    else -> {
                        // Keep current state but we could store movies for later use
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMovies() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.toLoading() }

            val lastPage = repository.getLastLoadedPage()
            currentPage = if (lastPage > 0) lastPage else 1

            if (lastPage == 0) {
                // First time loading
                loadMoviesFromApi(currentPage)
            } else {
                // Data exists in database, observeMovies() will handle UI update
                _uiState.update { it.toSuccess(_uiState.value.movies) }
            }
        }
    }

    private fun loadMoreMovies() {
        if (_uiState.value.isLoadingMore || isLastPage) return

        viewModelScope.launch {
            _uiState.update { it.toLoadingMore() }
            currentPage++
            loadMoviesFromApi(currentPage, isLoadingMore = true)
        }
    }

    private fun refreshMovies() {
        viewModelScope.launch {
            _uiState.update { it.toLoading() }
            repository.clearMovies()
            currentPage = 1
            isLastPage = false
            loadMoviesFromApi(currentPage)
        }
    }

    private suspend fun loadMoviesFromApi(page: Int, isLoadingMore: Boolean = false) {
        loadMoviesUseCase(page)
            .onSuccess {
                val hasMore = page < 500 // TMDB has limit
                isLastPage = !hasMore

                // The observeMovies() flow will update the UI with new movies
                // We just need to ensure the state reflects success
                _uiState.update { currentState ->
                    currentState.toSuccess(currentState.movies, hasMore)
                }
            }
            .onFailure { error ->
                _uiState.update { currentState ->
                    currentState.toError(
                        message = error.message ?: "Unknown error",
                        wasLoadingMore = isLoadingMore
                    )
                }
                _uiEvent.emit(MovieUiEvent.ShowError(error.message ?: "Unknown error"))

                // Revert page if it was loading more
                if (isLoadingMore) {
                    currentPage--
                }
            }
    }

    private fun clearError() {
        _uiState.update { it.clearError() }
    }
}