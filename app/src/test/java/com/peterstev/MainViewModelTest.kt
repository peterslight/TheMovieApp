package com.peterstev

import com.peterstev.domain.mock.MockData
import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.routing.Router
import com.peterstev.domain.usecase.ExclusionUseCase
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.SearchUseCase
import com.peterstev.util.RxSchedulers
import com.peterstev.viewmodel.MainViewModel
import io.mockk.*
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe


@ExperimentalCoroutinesApi
class MainViewModelTest : Spek({
    include(LiveDataSpek())

    describe("MainViewModel") {
        val searchUseCase: SearchUseCase by memoized { mockk() }
        val favouriteUseCase: FavouritesUseCase by memoized { mockk() }
        val router: Router by memoized { mockk() }
        val exclusionUseCase: ExclusionUseCase by memoized { mockk() }
        val schedulers: RxSchedulers by memoized {
            mockk {
                every { IO } returns Schedulers.trampoline()
                every { MAIN } returns Schedulers.trampoline()
            }
        }
        lateinit var viewModel: MainViewModel
        val movie = MockData.movie

        beforeEachTest {
            every { exclusionUseCase.getExcludedMovies() } returns Flowable.just(listOf(1, 2, 3, 4, 5))
            viewModel = MainViewModel(searchUseCase, favouriteUseCase, exclusionUseCase, router, schedulers)
        }

        describe("movie state") {
            val state: MutableList<MainViewModel.MovieState> = mutableListOf()
            beforeEachTest {
                viewModel.movieLiveData.observeForever { state.add(it) }
            }

            afterEachTest { state.clear() }

            context("Movie search") {
                var page = 1
                val query = "hitman"
                describe("a successful movie search") {
                    val result = MovieResult(
                        movies = mutableListOf(movie, movie, movie),
                        page = page,
                        totalResults = 100,
                        totalPages = 5
                    )

                    beforeEachTest {
                        every { searchUseCase.searchMovie(page, query) } returns Observable.just(result)
                    }

                    describe("searchMovie") {
                        beforeEachTest { viewModel.searchMovie(query) }

                        it("should contain the correct state") {
                            assertThat(state[1]).isInstanceOf(MainViewModel.MovieState.Success::class.java)
                        }

                        it("should contain the correct values") {
                            assertThat((state[1] as MainViewModel.MovieState.Success).data.movies).hasSize(3)
                            assertThat((state[1] as MainViewModel.MovieState.Success).data.totalResults).isEqualTo(100)
                            assertThat((state[1] as MainViewModel.MovieState.Success).data.totalPages).isEqualTo(5)
                        }
                    }
                }

                describe("a failed movie search") {
                    val errorMessage = "unable to find movie"
                    beforeEachTest {
                        every { searchUseCase.searchMovie(page, query) } returns
                                Observable.error(Exception(errorMessage))
                    }

                    describe("searchMovie") {
                        beforeEachTest { viewModel.searchMovie(query) }

                        it("should contain the correct state") {
                            assertThat(state[1]).isInstanceOf(MainViewModel.MovieState.Error::class.java)
                        }

                        it("should contain the correct error message") {
                            assertThat((state[1] as MainViewModel.MovieState.Error).throwable.message)
                                .isEqualTo("unable to find movie")
                        }
                    }
                }

                context("loading more data") {
                    describe("a successful fetch") {
                        val result = MovieResult(
                            movies = mutableListOf(movie, movie),
                            page = page,
                            totalResults = 10,
                            totalPages = 2
                        )

                        beforeEachTest {
                            viewModel.incrementPage()
                            page = viewModel.getCurrentPage()
                            every { searchUseCase.searchMovie(page, query) } returns Observable.just(result)
                            viewModel.searchMovie(query)
                        }

                        describe("loadMore") {
                            beforeEachTest {
                                viewModel.loadMore()
                            }

                            it("should contain the correct state") {
                                assertThat(state[1]).isInstanceOf(MainViewModel.MovieState.Success::class.java)
                            }

                            it("should contain the correct values") {
                                assertThat((state[1] as MainViewModel.MovieState.Success).data.movies).hasSize(2)
                                assertThat((state[1] as MainViewModel.MovieState.Success).data.totalResults).isEqualTo(10)
                                assertThat((state[1] as MainViewModel.MovieState.Success).data.totalPages).isEqualTo(2)
                            }

                            it("should return the correct page") {
                                assertThat(page).isEqualTo(2)
                            }
                        }
                    }

                    describe("a failed loadmore") {
                        val errorMessage = "unable to load more"
                        beforeEachTest {
                            viewModel.incrementPage()
                            page = viewModel.getCurrentPage()
                            every { searchUseCase.searchMovie(page, query) } returns
                                    Observable.error(Exception(errorMessage))
                            viewModel.searchMovie(query)
                        }

                        describe("loadMore") {
                            beforeEachTest { viewModel.loadMore() }

                            it("should contain the correct state") {
                                assertThat(state[1]).isInstanceOf(MainViewModel.MovieState.Error::class.java)
                            }

                            it("should contain the correct error message") {
                                assertThat((state[1] as MainViewModel.MovieState.Error).throwable.message)
                                    .isEqualTo("unable to load more")
                            }
                        }
                    }
                }

                describe("incrementPage") {
                    beforeEachTest {
                        viewModel.incrementPage()
                    }

                    it("should return a value of 2") {
                        assertThat(viewModel.getCurrentPage()).isEqualTo(2)
                    }
                }

                describe("resetPage") {
                    beforeEachTest {
                        viewModel.incrementPage()
                        viewModel.resetPage()
                    }

                    it("should return a value of 1") {
                        assertThat(viewModel.getCurrentPage()).isEqualTo(1)
                    }
                }
            }
        }

        context("favourites") {

            val state: MutableList<MainViewModel.FavouriteState> = mutableListOf()
            beforeEachTest {
                viewModel.favLiveData.observeForever { state.add(it) }
            }

            afterEachTest { state.clear() }

            describe("getting all favourites") {
                val favouriteList = mutableListOf(movie, movie, movie, movie)

                beforeEachTest {
                    every { favouriteUseCase.getFavourites() } returns Flowable.just(favouriteList)
                }

                describe("getFavourites") {
                    beforeEachTest {
                        viewModel.getFavourites()
                    }

                    it("should contain the correct state") {
                        assertThat(state[0]).isInstanceOf(MainViewModel.FavouriteState.OnLoad::class.java)
                    }

                    it("should contain the correct values") {
                        assertThat((state[0] as MainViewModel.FavouriteState.OnLoad).data).hasSize(4)
                    }
                }
            }

            describe("deleting a favourite") {
                beforeEachTest {
                    every { favouriteUseCase.deleteFavourite(movie) } returns Single.just(1)
                }

                describe("onFavouriteClick") {
                    beforeEachTest {
                        viewModel.onFavouriteClick(movie)
                    }

                    it("should contain the correct state") {
                        assertThat(state[0]).isInstanceOf(MainViewModel.FavouriteState.OnDelete::class.java)
                    }
                }
            }

            describe("error getting all favourites") {
                val message = "unable to get favourites"

                beforeEachTest {
                    every { favouriteUseCase.getFavourites() } returns Flowable.error(Exception(message))
                }

                describe("getFavourites") {
                    beforeEachTest {
                        viewModel.getFavourites()
                    }

                    it("should contain the correct state") {
                        assertThat(state[0]).isInstanceOf(MainViewModel.FavouriteState.OnError::class.java)
                    }

                    it("should contain the correct values") {
                        assertThat((state[0] as MainViewModel.FavouriteState.OnError).throwable.message)
                            .isEqualTo("unable to get favourites")
                    }
                }
            }
        }

        describe("toDetail") {
            beforeEachTest {
                every { router.toDetail(movie) } just runs
                viewModel.onListItemClick(movie)
            }

            it("should navigate to detail page") {
                verify { router.toDetail(movie) }
            }
        }
    }
})































