package com.peterstev.domain.inversion

import com.peterstev.domain.model.Movie
import com.peterstev.domain.repository.ExclusionRepository
import com.peterstev.domain.usecase.ExclusionUseCase
import javax.inject.Inject

class ExclusionUseCaseImpl @Inject constructor(
    private val repository: ExclusionRepository,
) : ExclusionUseCase {

    override fun excludeMovie(movie: Movie) = repository.excludeMovie(movie)

    override fun includeMovie(movie: Movie) = repository.includeMovie(movie)

    override fun getExcludedMovies() = repository.getExcludedMovies()

    override fun resetExclusionList() = repository.resetExclusionList()
}
