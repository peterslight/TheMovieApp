package com.peterstev.database.inversion

import com.peterstev.database.dao.ExclusionDao
import com.peterstev.database.transform.ExclusionEntityMapper
import com.peterstev.domain.model.Movie
import com.peterstev.domain.repository.ExclusionRepository
import io.reactivex.rxjava3.core.Flowable
import javax.inject.Inject

class ExclusionRepositoryImpl @Inject constructor(
    private val exclusionDao: ExclusionDao,
    private val mapper: ExclusionEntityMapper,
) : ExclusionRepository {

    override fun excludeMovie(movie: Movie) = exclusionDao.exclude(mapper.transform(movie))

    override fun includeMovie(movie: Movie) = exclusionDao.include(mapper.transform(movie))

    override fun getExcludedMovies(): Flowable<List<Int>> = exclusionDao
        .getExcludedMovies()
        .map { mapper.transform(it) }

    override fun resetExclusionList() = exclusionDao.resetExclusionList()
}
