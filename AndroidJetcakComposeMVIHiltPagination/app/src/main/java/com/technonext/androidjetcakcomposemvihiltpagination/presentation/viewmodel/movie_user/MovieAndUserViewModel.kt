package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.movie_user

// 📄 presentation/viewmodel/MovieAndUserViewModel.kt

// 📄 presentation/viewmodel/MovieAndUserViewModel.kt (Corrected)


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.DataState
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.LoadMoviesUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetUsersUseCase
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiState
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserIntent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiEvent
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.user.UserUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieAndUserViewModel @Inject constructor(
    // Movie dependencies
    private val getMoviesUseCase: GetMoviesUseCase,
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val movieRepository: MovieRepository,
    // User dependencies
    private val getUsersUseCase: GetUsersUseCase
) : ViewModel() {

    // Movie State Management
    private val _movieUiState = MutableStateFlow(MovieUiState())
    val movieUiState: StateFlow<MovieUiState> = _movieUiState.asStateFlow()

    private val _movieUiEvent = MutableSharedFlow<MovieUiEvent>()
    val movieUiEvent: SharedFlow<MovieUiEvent> = _movieUiEvent.asSharedFlow()

    // User State Management
    private val _userUiState = MutableStateFlow(UserUiState())
    val userUiState: StateFlow<UserUiState> = _userUiState.asStateFlow()

    private val _userUiEvent = MutableSharedFlow<UserUiEvent>()
    val userUiEvent: SharedFlow<UserUiEvent> = _userUiEvent.asSharedFlow()

    // Movie-specific variables
    private var currentPage = 1
    private var isLastPage = false

    init {
        observeMovies()
        handleMovieIntent(MovieIntent.LoadMovies)
        handleUserIntent(UserIntent.LoadUsers)
    }

    // Movie Intent Handling
    fun handleMovieIntent(intent: MovieIntent) {
        when (intent) {
            is MovieIntent.LoadMovies -> loadMovies()
            is MovieIntent.LoadMoreMovies -> loadMoreMovies()
            is MovieIntent.RefreshMovies -> refreshMovies()
            is MovieIntent.ClearError -> clearMovieError()
        }
    }

    // User Intent Handling
    fun handleUserIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
            is UserIntent.RefreshUsers -> refreshUsers()
            is UserIntent.ClearError -> clearUserError()
        }
    }

    // Convenience methods
    fun refreshAll() {
        refreshMovies()
        refreshUsers()
    }

    fun clearAllErrors() {
        clearMovieError()
        clearUserError()
    }



    // Movie Implementation
    private fun observeMovies() {
        getMoviesUseCase()
            .onEach { movies ->
                val currentState = _movieUiState.value.state
                when (currentState) {
                    is DataState.Success -> {
                        _movieUiState.update { it.toSuccess(movies, currentState.hasMoreData) }
                    }
                    is DataState.Idle -> {
                        if (movies.isNotEmpty()) {
                            _movieUiState.update { it.toSuccess(movies) }
                        }
                    }
                    else -> {
                        // Keep current state during loading
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMovies() {
        if (_movieUiState.value.isLoading) return

        viewModelScope.launch {
            _movieUiState.update { it.toLoading() }

            val lastPage = movieRepository.getLastLoadedPage()
            currentPage = if (lastPage > 0) lastPage else 1

            if (lastPage == 0) {
                loadMoviesFromApi(currentPage)
            } else {
                _movieUiState.update { it.toSuccess(_movieUiState.value.movies) }
            }
        }
    }

    fun loadMoreMovies() {
        if (_movieUiState.value.isLoadingMore || isLastPage) return

        viewModelScope.launch {
            _movieUiState.update { it.toLoadingMore() }
            currentPage++
            loadMoviesFromApi(currentPage, isLoadingMore = true)
        }
    }

    fun refreshMovies() {
        viewModelScope.launch {
            _movieUiState.update { it.toLoading() }
            movieRepository.clearMovies()
            currentPage = 1
            isLastPage = false
            loadMoviesFromApi(currentPage)
        }
    }

    private suspend fun loadMoviesFromApi(page: Int, isLoadingMore: Boolean = false) {
        loadMoviesUseCase(page)
            .onSuccess {
                val hasMore = page < 500
                isLastPage = !hasMore
                _movieUiState.update { currentState ->
                    currentState.toSuccess(currentState.movies, hasMore)
                }
            }
            .onFailure { error ->
                _movieUiState.update { currentState ->
                    currentState.toError(
                        message = error.message ?: "Unknown error",
                        wasLoadingMore = isLoadingMore
                    )
                }
                _movieUiEvent.emit(MovieUiEvent.ShowError(error.message ?: "Unknown error"))

                if (isLoadingMore) {
                    currentPage--
                }
            }
    }

    fun clearMovieError() {
        _movieUiState.update { it.clearError() }
    }

    // User Implementation
    private fun loadUsers() {
        if (_userUiState.value.isLoading) return

        viewModelScope.launch {
            _userUiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    fun refreshUsers() {
        viewModelScope.launch {
            _userUiState.update { it.toLoading() }
            loadUsersFromApi()
        }
    }

    private suspend fun loadUsersFromApi() {
        getUsersUseCase()
            .onSuccess { users ->
                _userUiState.update { it.toSuccess(users) }
            }
            .onFailure { error ->
                _userUiState.update { it.toError(error.message ?: "Unknown error") }
                _userUiEvent.emit(UserUiEvent.ShowError(error.message ?: "Unknown error"))
            }
    }

    fun clearUserError() {
        _userUiState.update { it.clearError() }
    }
}