package com.technonext.androidjetcakcomposemvihiltpagination.domain.usecase

import com.technonext.androidjetcakcomposemvihiltpagination.domain.model.Movie
import com.technonext.androidjetcakcomposemvihiltpagination.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
    }
}
