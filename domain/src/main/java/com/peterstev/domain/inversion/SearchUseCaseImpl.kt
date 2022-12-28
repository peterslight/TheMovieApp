package com.peterstev.domain.inversion

import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.repository.SearchRepository
import com.peterstev.domain.usecase.SearchUseCase
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchUseCaseImpl @Inject constructor(
    private val repository: SearchRepository,
) : SearchUseCase {

    override fun searchMovie(page: Int, query: String): Observable<MovieResult> {
        return repository.searchMovie(page, query)
    }
}
