package com.peterstev.domain

import com.peterstev.domain.inversion.FavouritesUseCaseImpl
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.model.Movie
import com.peterstev.domain.repository.FavouriteRepository
import com.peterstev.domain.usecase.FavouritesUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class FavouritesUseCaseTest : Spek({

    describe("FavouriteUseCase") {
        val repository: FavouriteRepository by memoized { mockk() }
        lateinit var usecase: FavouritesUseCase

        beforeEachTest {
            usecase = FavouritesUseCaseImpl(repository)
        }

        val movie = MockData.movie

        describe("addFavourite") {
            lateinit var result: Single<Long>
            beforeEachTest {
                every { repository.addFavourite(movie) } returns Single.just(1L)
                result = usecase.addFavourite(movie)
            }

            it("should return a value of 1") {
                assertThat(result.blockingGet()).isEqualTo(1L)
            }
        }

        describe("getFavourites") {
            lateinit var result: Flowable<List<Movie>>
            beforeEachTest {
                every { repository.getFavourites() } returns Flowable.just(
                    listOf(movie, movie, movie)
                )
                result = usecase.getFavourites()
            }

            it("should return correct movie list size") {
                assertThat(result.blockingFirst().size).isEqualTo(3)
            }
        }

        describe("getSingleFavourite") {
            lateinit var result: Single<Movie>
            val id = 10
            beforeEachTest {
                every { repository.getSingleFavourite(id) } returns Single.just(movie)
                result = usecase.getSingleFavourite(id)
            }

            it("should return a valid movie") {
                result.blockingGet().run {
                    assertThat(posterPath).isEqualTo("postPath")
                    assertThat(overview).isEqualTo("overview")
                    assertThat(voteCount).isEqualTo(4)
                    assertThat(voteAverage).isEqualTo(1.1)
                }
            }
        }

        describe("deleteFavourite") {
            lateinit var result: Single<Int>
            beforeEachTest {
                every { repository.deleteFavourite(movie) } returns Single.just(2)
                result = usecase.deleteFavourite(movie)
            }

            it("should return a value of 2") {
                assertThat(result.blockingGet()).isEqualTo(2)
            }
        }

        describe("deleteAllFavourites") {
            lateinit var result: Any
            beforeEachTest {
                every { repository.deleteAllFavourites() } returns Completable.complete()
                result = usecase.deleteAllFavourites()
            }

            it("should return a completable object") {
                result.let {
                    assertThat(it).isInstanceOf(Completable::class.java)
                }
            }
        }
    }
})
