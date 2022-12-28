package com.peterstev.data.inversion

import com.peterstev.data.network.BaseApiService
import com.peterstev.data.mapper.MovieMapper
import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.repository.SearchRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val service: BaseApiService,
    private val mapper: MovieMapper,
) : SearchRepository {

    override fun searchMovie(page: Int, query: String): Observable<MovieResult> {
        return service.searchMovie(
            apiKey = "System.getenv(yourApiKey)",
            text = query,
            page = page
        )
            .map { mapper.transform(it) }
    }
}
