package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.LoadMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val repository: MovieRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()
    private val _uiEvent = MutableSharedFlow<MovieUiEvent>()
    val uiEvent: SharedFlow<MovieUiEvent> = _uiEvent.asSharedFlow()
    private var currentPage = 1
    private var isLastPage = false
    init {
        observeMovies()
        handleIntent(MovieIntent.LoadMovies)
    }
    fun handleIntent(intent: MovieIntent) {
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
                _uiState.update { it.copy(movies = movies) }
            }
            .launchIn(viewModelScope)
    }
    private fun loadMovies() {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val lastPage = repository.getLastLoadedPage()
            currentPage = if (lastPage > 0) lastPage else 1
            if (lastPage == 0) {
// First time loading
                loadMoviesFromApi(currentPage)
            } else {
// Data exists, just update loading state
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
    private fun loadMoreMovies() {
        if (_uiState.value.isLoadingMore || isLastPage) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true, error = null) }
            currentPage++
            loadMoviesFromApi(currentPage, isLoadingMore = true)
        }
    }
    private fun refreshMovies() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.clearMovies()
            currentPage = 1
            isLastPage = false
            loadMoviesFromApi(currentPage)
        }
    }

    private suspend fun loadMoviesFromApi(page: Int, isLoadingMore: Boolean = false) {
        loadMoviesUseCase(page)
            .onSuccess {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = null,
                        hasMoreData = page < 500 // TMDB has limit
                    )
                }
            }
            .onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isLoadingMore = false,
                        error = error.message
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
        _uiState.update { it.copy(error = null) }
    }
}