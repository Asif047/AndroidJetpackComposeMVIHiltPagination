//package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation
//
//// 📄 presentation/viewmodel/delegate/MovieViewModelDelegateImpl.kt
//
//
//import androidx.lifecycle.viewModelScope
//import com.technonext.androidjetcakcomposemvihiltpagination.core.ui.DataState
//import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
//import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.GetMoviesUseCase
//import com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase.LoadMoviesUseCase
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieIntent
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiEvent
//import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.MovieUiState
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.flow.MutableSharedFlow
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.SharedFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asSharedFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.launchIn
//import kotlinx.coroutines.flow.onEach
//import kotlinx.coroutines.flow.update
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//class MovieViewModelDelegateImpl @Inject constructor(
//    private val getMoviesUseCase: GetMoviesUseCase,
//    private val loadMoviesUseCase: LoadMoviesUseCase,
//    private val repository: MovieRepository
//) : MovieViewModelDelegate {
//
//    private val _movieUiState = MutableStateFlow(MovieUiState())
//    override val movieUiState: StateFlow<MovieUiState> = _movieUiState.asStateFlow()
//
//    private val _movieUiEvent = MutableSharedFlow<MovieUiEvent>()
//    override val movieUiEvent: SharedFlow<MovieUiEvent> = _movieUiEvent.asSharedFlow()
//
//    private var currentPage = 1
//    private var isLastPage = false
//    private lateinit var coroutineScope: CoroutineScope
//
//    fun initialize(scope: CoroutineScope) {
//        coroutineScope = scope
//        observeMovies()
//        handleMovieIntent(MovieIntent.LoadMovies)
//    }
//
//    override fun handleMovieIntent(intent: MovieIntent) {
//        when (intent) {
//            is MovieIntent.LoadMovies -> loadMovies()
//            is MovieIntent.LoadMoreMovies -> loadMoreMovies()
//            is MovieIntent.RefreshMovies -> refreshMovies()
//            is MovieIntent.ClearError -> {
//
//            }
//        }
//    }
//
//    override fun loadMoreMovies() {
//        if (_movieUiState.value.isLoadingMore || isLastPage) return
//
//        coroutineScope.launch {
//            _movieUiState.update { it.toLoadingMore() }
//            currentPage++
//            loadMoviesFromApi(currentPage, isLoadingMore = true)
//        }
//    }
//
//    override fun refreshMovies() {
//        coroutineScope.launch {
//            _movieUiState.update { it.toLoading() }
//            repository.clearMovies()
//            currentPage = 1
//            isLastPage = false
//            loadMoviesFromApi(currentPage)
//        }
//    }
//
//    override fun clearMovieError() {
//        _movieUiState.update { it.clearError() }
//    }
//
//    private fun observeMovies() {
//        getMoviesUseCase()
//            .onEach { movies ->
//                val currentState = _movieUiState.value.state
//                when (currentState) {
//                    is DataState.Success -> {
//                        _movieUiState.update { it.toSuccess(movies, currentState.hasMoreData) }
//                    }
//                    is DataState.Idle -> {
//                        if (movies.isNotEmpty()) {
//                            _movieUiState.update { it.toSuccess(movies) }
//                        }
//                    }
//                    else -> {
//                        // Keep current state during loading
//                    }
//                }
//            }
//            .launchIn(coroutineScope)
//    }
//
//    private fun loadMovies() {
//        if (_movieUiState.value.isLoading) return
//
//        coroutineScope.launch {
//            _movieUiState.update { it.toLoading() }
//
//            val lastPage = repository.getLastLoadedPage()
//            currentPage = if (lastPage > 0) lastPage else 1
//
//            if (lastPage == 0) {
//                loadMoviesFromApi(currentPage)
//            } else {
//                _movieUiState.update { it.toSuccess(_movieUiState.value.movies) }
//            }
//        }
//    }
//
//    private suspend fun loadMoviesFromApi(page: Int, isLoadingMore: Boolean = false) {
//        loadMoviesUseCase(page)
//            .onSuccess {
//                val hasMore = page < 500 // TMDB has limit
//                isLastPage = !hasMore
//
//                _movieUiState.update { currentState ->
//                    currentState.toSuccess(currentState.movies, hasMore)
//                }
//            }
//            .onFailure { error ->
//                _movieUiState.update { currentState ->
//                    currentState.toError(
//                        message = error.message ?: "Unknown error",
//                        wasLoadingMore = isLoadingMore
//                    )
//                }
//                _movieUiEvent.emit(MovieUiEvent.ShowError(error.message ?: "Unknown error"))
//
//                if (isLoadingMore) {
//                    currentPage--
//                }
//            }
//    }
//}