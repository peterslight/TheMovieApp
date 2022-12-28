package com.peterstev.domain.repository

import com.peterstev.domain.model.MovieResult
import io.reactivex.rxjava3.core.Observable

interface SearchRepository {

    fun searchMovie(page: Int, query: String): Observable<MovieResult>
}
