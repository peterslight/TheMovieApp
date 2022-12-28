package com.peterstev.database

import com.peterstev.database.dao.ExclusionDao
import com.peterstev.database.entity.ExclusionEntity
import com.peterstev.database.inversion.ExclusionRepositoryImpl
import com.peterstev.database.transform.ExclusionEntityMapper
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.repository.ExclusionRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class ExclusionRepositoryTest : Spek({

    describe("ExclusionRepository") {
        val dao: ExclusionDao by memoized { mockk() }
        val mapper: ExclusionEntityMapper by memoized { mockk() }
        lateinit var repository: ExclusionRepository
        val movie = MockData.movie
        val entity: ExclusionEntity = mockk()

        beforeEachTest {
            repository = ExclusionRepositoryImpl(dao, mapper)
            every { mapper.transform(movie) } returns entity
        }

        describe("excluding a Movie") {
            beforeEachTest {
                every { dao.exclude(entity) } returns Single.just(1L)
            }

            describe("excludeMovie") {
                it("should return correct values") {
                    repository
                        .excludeMovie(movie)
                        .blockingGet()
                        .let {
                            assertThat(it).isEqualTo(1L)
                        }
                }
            }
        }

        describe("including a Movie") {
            beforeEachTest {
                every { dao.include(entity) } returns Single.just(1)
            }

            describe("includeMovie") {
                it("should return correct values") {
                    repository
                        .includeMovie(movie)
                        .blockingGet()
                        .let {
                            assertThat(it).isEqualTo(1)
                        }
                }
            }
        }

        describe("getting excluded MovieList") {
            beforeEachTest {
                every { dao.getExcludedMovies() } returns Flowable.just(listOf(entity, entity))
                every { mapper.transform(listOf(entity, entity)) } returns listOf(1, 2)
            }

            describe("getExcludedMovies") {
                it("should return correct size") {
                    repository
                        .getExcludedMovies()
                        .blockingFirst()
                        .size.let { assertThat(it).isEqualTo(2) }
                }
            }
        }

        describe("resetting an exclusion List") {
            beforeEachTest {
                every { dao.resetExclusionList() } returns Completable.complete()
            }

            describe("resetExclusionList") {
                it("should return a completable") {
                    repository
                        .resetExclusionList()
                        .let { assertThat(it).isInstanceOf(Completable::class.java) }
                }
            }
        }
    }
})
