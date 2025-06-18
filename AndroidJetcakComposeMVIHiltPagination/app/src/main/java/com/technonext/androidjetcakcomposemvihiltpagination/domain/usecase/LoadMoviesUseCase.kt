package com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase

import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import javax.inject.Inject

class LoadMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(page: Int): Result<Unit> {
        return repository.loadMovies(page)
    }
}