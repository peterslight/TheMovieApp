package com.peterstev.database.inversion

import com.peterstev.database.dao.MovieDao
import com.peterstev.database.transform.MovieEntityMapper
import com.peterstev.domain.model.Movie
import com.peterstev.domain.repository.FavouriteRepository
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val entityMapper: MovieEntityMapper,
) : FavouriteRepository {

    override fun addFavourite(movie: Movie) = movieDao.addFavourite(entityMapper.transform(movie))

    override fun getFavourites(): Flowable<List<Movie>> =
        movieDao.getFavourites()
            .map { entityMapper.transform(it) }

    override fun getSingleFavourite(id: Int): Single<Movie> =
        movieDao.getSingleFavourite(id)
            .map { entityMapper.transform(it) }

    override fun deleteFavourite(movie: Movie) =
        movieDao.deleteFavourite(entityMapper.transform(movie))

    override fun deleteAllFavourites() = movieDao.deleteAllFavourites()
}
