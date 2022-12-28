package com.peterstev.data

import com.peterstev.data.inversion.SearchRepositoryImpl
import com.peterstev.data.mapper.MovieMapper
import com.peterstev.data.models.JsonMovieResult
import com.peterstev.data.network.BaseApiService
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.repository.SearchRepository
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SearchRepositoryTest : Spek({

    describe("SearchRepository") {
        val service: BaseApiService by memoized { mockk() }
        val mapper: MovieMapper by memoized { mockk() }
        lateinit var repository: SearchRepository
        beforeEachTest {
            repository = SearchRepositoryImpl(service, mapper)
        }

        describe("BaseApiService") {
            val page = 1
            val apikey = "System.getenv(yourApiKey)"
            val query = "black panther"
            val jsonMovieResult = mockk<JsonMovieResult>()
            val movieResult = MockData.movieResult

            beforeEachTest {
                every { service.searchMovie(apikey, query, page) } returns Observable.just(jsonMovieResult)
                every { mapper.transform(jsonMovieResult) } returns movieResult
            }

            describe("searchMovie") {
                lateinit var result: Observable<MovieResult>
                beforeEachTest {
                    result = repository.searchMovie(page, query)
                }

                it("should return a non null value") {
                    assertThat(result.blockingFirst()).isNotNull
                }

                it("should return an Observable<MovieResult>") {
                    assertThat(result.blockingFirst()).isInstanceOf(MovieResult::class.java)
                }

                it("should return the correct values") {
                    result.blockingFirst().run {
                        assertThat(totalResults).isEqualTo(10)
                        assertThat(totalPages).isEqualTo(5)
                        assertThat(page).isEqualTo(1)
                        assertThat(movies).hasSize(3)
                    }
                }
            }
        }
    }
})
