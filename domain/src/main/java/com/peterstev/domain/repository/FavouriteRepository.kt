package com.peterstev.domain.repository

import com.peterstev.domain.model.Movie
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface FavouriteRepository {

    fun addFavourite(movie: Movie): Single<Long>

    fun getFavourites(): Flowable<List<Movie>>

    fun getSingleFavourite(id: Int): Single<Movie>

    fun deleteFavourite(movie: Movie): Single<Int>

    fun deleteAllFavourites(): Completable
}
