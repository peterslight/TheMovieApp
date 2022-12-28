package com.peterstev

import com.peterstev.domain.mock.MockData
import com.peterstev.domain.usecase.ExclusionUseCase
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.util.RxSchedulers
import com.peterstev.viewmodel.DetailViewModel
import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

@ExperimentalCoroutinesApi
class DetailViewModelTest : Spek({
    include(LiveDataSpek())

    describe("DetailViewModel") {
        val exclusionUseCase: ExclusionUseCase by memoized { mockk() }
        val favouriteUseCase: FavouritesUseCase by memoized { mockk() }
        val schedulers: RxSchedulers by memoized {
            mockk {
                every { IO } returns Schedulers.trampoline()
                every { MAIN } returns Schedulers.trampoline()
            }
        }
        lateinit var viewModel: DetailViewModel
        beforeEachTest {
            viewModel = DetailViewModel(favouriteUseCase, exclusionUseCase, schedulers)
        }

        val movie = MockData.movie

        describe("exclusion state") {
            val state: MutableList<DetailViewModel.ExclusionState> = mutableListOf()
            beforeEachTest {
                viewModel.exclusionLiveData.observeForever { state.add(it) }
            }

            afterEachTest { state.clear() }

            context("movie exclusion") {
                describe("excluding a movie") {
                    beforeEachTest {
                        every { exclusionUseCase.excludeMovie(movie) } returns
                                Single.just(1L)
                    }

                    describe("onExcludeClick") {
                        beforeEachTest {
                            viewModel.onExcludeClick(movie)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.ExclusionState.OnExclude::class.java)
                        }
                    }
                }

                describe("including a movie") {
                    beforeEachTest {
                        every { exclusionUseCase.includeMovie(movie) } returns
                                Single.just(1)
                    }

                    describe("onIncludeClick") {
                        beforeEachTest { viewModel.onIncludeClick(movie) }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.ExclusionState.OnInclude::class.java)
                        }
                    }
                }
            }
        }
        describe("favourite state") {
            val state: MutableList<DetailViewModel.FavouriteState> = mutableListOf()
            beforeEachTest {
                viewModel.favouriteLiveData.observeForever { state.add(it) }
            }

            afterEachTest { state.clear() }

            context("favourites") {
                describe("adding a favourite movie") {
                    beforeEachTest {
                        every { favouriteUseCase.addFavourite(movie) } returns
                                Single.just(1L)
                    }

                    describe("onAddFavClick") {
                        beforeEachTest {
                            viewModel.onAddFavClick(movie)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.OnFavouriteAdded::class.java)
                        }
                    }
                }

                describe("removing a favourite movie") {
                    beforeEachTest {
                        every { favouriteUseCase.deleteFavourite(movie) } returns
                                Single.just(1)
                    }

                    describe("onRemoveFavClick") {
                        beforeEachTest {
                            viewModel.onRemoveFavClick(movie)
                        }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.OnFavouriteRemoved::class.java)
                        }
                    }
                }

                describe("success getting a movie from favourites") {
                    beforeEachTest {
                        movie.isFavourite = true
                        every { favouriteUseCase.getSingleFavourite(movie.id) } returns
                                Single.just(movie)
                    }

                    describe("getSingleMovie") {
                        beforeEachTest { viewModel.getSingleMovie(movie) }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.OnFavouriteAdded::class.java)
                        }
                    }
                }

                describe("failure getting a movie from favourites") {
                    beforeEachTest {
                        every { favouriteUseCase.getSingleFavourite(movie.id) } returns Single.just(mockk())
                    }

                    describe("getSingleMovie") {
                        beforeEachTest { viewModel.getSingleMovie(movie) }

                        it("should contain the correct state") {
                            assertThat(state[0]).isInstanceOf(DetailViewModel.FavouriteState.OnFavouriteRemoved::class.java)
                        }
                    }
                }
            }
        }
    }
})
