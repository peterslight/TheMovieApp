package com.peterstev.domain

import com.peterstev.domain.inversion.ExclusionUseCaseImpl
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.repository.ExclusionRepository
import com.peterstev.domain.usecase.ExclusionUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ExclusionUseCaseTest : Spek({

    describe("ExclusionUseCase") {
        val repository: ExclusionRepository by memoized { mockk() }
        lateinit var usecase: ExclusionUseCase

        beforeEachTest {
            usecase = ExclusionUseCaseImpl(repository)
        }

        val movie = MockData.movie

        describe("excludeMovie") {
            lateinit var result: Single<Long>
            beforeEachTest {
                every { repository.excludeMovie(movie) } returns Single.just(1L)
                result = usecase.excludeMovie(movie)
            }

            it("should return a value of 1") {
                assertThat(result.blockingGet()).isEqualTo(1L)
            }
        }

        describe("includeMovie") {
            lateinit var result: Single<Int>
            beforeEachTest {
                every { repository.includeMovie(movie) } returns Single.just(2)
                result = usecase.includeMovie(movie)
            }

            it("should return a value of 2") {
                assertThat(result.blockingGet()).isEqualTo(2)
            }
        }

        describe("getExcludedMovies") {
            lateinit var result: Flowable<List<Int>>
            beforeEachTest {
                every { repository.getExcludedMovies() } returns Flowable.just(listOf(1, 2, 3))
                result = usecase.getExcludedMovies()
            }

            it("should return correct exclusion list size") {
                assertThat(result.blockingFirst().size).isEqualTo(3)
            }
        }

        describe("resetExclusionList") {
            lateinit var result: Any
            beforeEachTest {
                every { repository.resetExclusionList() } returns Completable.complete()
                result = usecase.resetExclusionList()
            }

            it("should return a completable object") {
                result.let {
                    assertThat(it).isInstanceOf(Completable::class.java)
                }
            }
        }
    }
})
