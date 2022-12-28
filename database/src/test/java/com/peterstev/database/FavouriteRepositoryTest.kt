package com.peterstev.database

import com.peterstev.database.dao.MovieDao
import com.peterstev.database.entity.MovieEntity
import com.peterstev.database.inversion.FavouriteRepositoryImpl
import com.peterstev.database.transform.MovieEntityMapper
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.repository.FavouriteRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class FavouriteRepositoryTest : Spek({

    describe("FavouriteRepository") {
        val dao: MovieDao by memoized { mockk() }
        val mapper: MovieEntityMapper by memoized { mockk() }
        lateinit var repository: FavouriteRepository
        val movie = MockData.movie
        val entity: MovieEntity = mockk()

        beforeEachTest {
            repository = FavouriteRepositoryImpl(dao, mapper)
            every { mapper.transform(movie) } returns entity
        }

        describe("adding a favourite") {
            beforeEachTest {
                every { dao.addFavourite(entity) } returns Single.just(1L)
            }

            describe("addFavourite") {
                it("should return correct values") {
                    repository
                        .addFavourite(movie)
                        .blockingGet()
                        .let {
                            assertThat(it).isEqualTo(1L)
                        }
                }
            }
        }

        describe("getting a single favourite") {
            beforeEachTest {
                every { dao.getSingleFavourite(movie.id) } returns Single.just(entity)
                every { mapper.transform(entity) } returns movie
            }

            describe("getSingleFavourite") {
                it("should return correct values") {
                    repository
                        .getSingleFavourite(movie.id)
                        .blockingGet()
                        .run {
                            assertThat(posterPath).isEqualTo("postPath")
                            assertThat(overview).isEqualTo("overview")
                            assertThat(voteCount).isEqualTo(4)
                            assertThat(voteAverage).isEqualTo(1.1)
                        }
                }
            }
        }

        describe("delete a favourite") {
            beforeEachTest {
                every { dao.deleteFavourite(entity) } returns Single.just(1)
            }

            describe("deleteFavourite") {
                it("should return correct values") {
                    repository
                        .deleteFavourite(movie)
                        .blockingGet()
                        .let {
                            assertThat(it).isEqualTo(1)
                        }
                }
            }
        }

        describe("getting favourite MovieList") {
            beforeEachTest {
                every { dao.getFavourites() } returns Flowable.just(listOf(entity, entity))
                every { mapper.transform(listOf(entity, entity)) } returns listOf(movie, movie, movie)
            }

            describe("getFavourites") {
                it("should return correct size") {
                    repository
                        .getFavourites()
                        .blockingFirst()
                        .size.let { assertThat(it).isEqualTo(3) }
                }
            }
        }

        describe("deleting all favourites") {
            beforeEachTest {
                every { dao.deleteAllFavourites() } returns Completable.complete()
            }

            describe("deleteAllFavourites") {
                it("should return a completable") {
                    repository
                        .deleteAllFavourites()
                        .let { assertThat(it).isInstanceOf(Completable::class.java) }
                }
            }
        }
    }
})
