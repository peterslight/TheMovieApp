package com.peterstev.domain.usecase

import com.peterstev.domain.model.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface ExclusionUseCase {

    fun excludeMovie(movie: Movie): Single<Long>

    fun includeMovie(movie: Movie): Single<Int>

    fun getExcludedMovies(): Flowable<List<Int>>

    fun resetExclusionList(): Completable
}
