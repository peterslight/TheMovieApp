package com.peterstev.domain.inversion

import com.peterstev.domain.model.Movie
import com.peterstev.domain.repository.FavouriteRepository
import com.peterstev.domain.usecase.FavouritesUseCase
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavouritesUseCaseImpl @Inject constructor(
    private val repository: FavouriteRepository,
) : FavouritesUseCase {

    override fun addFavourite(movie: Movie): Single<Long> {
        return repository.addFavourite(movie)
    }

    override fun getFavourites(): Flowable<List<Movie>> {
        return repository.getFavourites()
    }

    override fun getSingleFavourite(id: Int): Single<Movie> {
        return repository.getSingleFavourite(id)
    }

    override fun deleteFavourite(movie: Movie): Single<Int> {
        return repository.deleteFavourite(movie)
    }

    override fun deleteAllFavourites(): Completable {
        return repository.deleteAllFavourites()
    }
}
