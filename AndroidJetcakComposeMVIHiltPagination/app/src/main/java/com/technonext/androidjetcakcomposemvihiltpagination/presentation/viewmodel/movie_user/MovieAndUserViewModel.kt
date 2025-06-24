package com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.movie_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.MovieViewModelDelegate
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.MovieViewModelDelegateImpl
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.UserViewModelDelegate
import com.technonext.androidjetcakcomposemvihiltpagination.presentation.viewmodel.delegation.UserViewModelDelegateImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// 📄 presentation/viewmodel/MovieAndUserViewModel.kt

// 📄 presentation/viewmodel/MovieAndUserViewModel.kt (Corrected)

// 📄 presentation/viewmodel/MovieAndUserViewModel.kt


@HiltViewModel
class MovieAndUserViewModel @Inject constructor(
    private val movieDelegateImpl: MovieViewModelDelegateImpl,
    private val userDelegateImpl: UserViewModelDelegateImpl
) : ViewModel(),
    MovieViewModelDelegate by movieDelegateImpl,
    UserViewModelDelegate by userDelegateImpl {

    init {
        // Initialize both delegates with the ViewModel's scope
        movieDelegateImpl.initialize(viewModelScope)
        userDelegateImpl.initialize(viewModelScope)
    }

    // Additional combined functionality if needed
    fun refreshAll() {
        refreshMovies()
        refreshUsers()
    }

    fun clearAllErrors() {
        clearMovieError()
        clearUserError()
    }
}