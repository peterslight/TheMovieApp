package com.peterstev.domain

import com.peterstev.domain.inversion.SearchUseCaseImpl
import com.peterstev.domain.mock.MockData
import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.repository.SearchRepository
import com.peterstev.domain.usecase.SearchUseCase
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

class SearchUseCaseTest : Spek({

    describe("SearchUseCase") {
        val repository: SearchRepository by memoized { mockk() }
        lateinit var usecase: SearchUseCase

        beforeEachTest {
            usecase = SearchUseCaseImpl(repository)
        }

        val movieResult = MockData.movieResult

        describe("searchMovie") {
            lateinit var result: Observable<MovieResult>
            val page = 1
            val query = "spiderman"
            beforeEachTest {
                every { repository.searchMovie(page, query) } returns Observable.just(movieResult)
                result = usecase.searchMovie(page, query)
            }

            it("should return correct values") {
                result.blockingSingle().run {
                    assertThat(totalPages).isEqualTo(5)
                    assertThat(page).isEqualTo(1)
                    assertThat(totalResults).isEqualTo(10)
                    assertThat(movies).hasSize(3)
                    assertThat(movies.first().id).isEqualTo(1234)
                }
            }
        }
    }
})











