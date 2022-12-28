package com.peterstev.domain.usecase

import com.peterstev.domain.model.MovieResult
import io.reactivex.rxjava3.core.Observable

interface SearchUseCase {

    fun searchMovie(page: Int, query: String): Observable<MovieResult>
}
