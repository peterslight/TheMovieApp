package com.peterstev.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peterstev.domain.model.Movie
import com.peterstev.domain.model.MovieResult
import com.peterstev.domain.routing.Router
import com.peterstev.domain.usecase.ExclusionUseCase
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.SearchUseCase
import com.peterstev.util.RxSchedulers
import com.peterstev.util.thread
import com.peterstev.viewmodel.MainViewModel.FavouriteState.OnLoad
import com.peterstev.viewmodel.MainViewModel.MovieState.LoadMoreProgress
import com.peterstev.viewmodel.MainViewModel.MovieState.Loading
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val favouritesUseCase: FavouritesUseCase,
    exclusionUseCase: ExclusionUseCase,
    private val router: Router,
    private val schedulers: RxSchedulers,
) : ViewModel() {

    private val mutableMovieLiveData = MutableLiveData<MovieState>()
    val movieLiveData: LiveData<MovieState> = mutableMovieLiveData

    private val mutableFavLiveData = MutableLiveData<FavouriteState>()
    val favLiveData: LiveData<FavouriteState> = mutableFavLiveData

    private val disposable by lazy { CompositeDisposable() }
    private var page = 1
    private lateinit var query: String
    lateinit var exclusionList: MutableList<Int>

    sealed class MovieState {
        class Success(val data: MovieResult) : MovieState()
        class Error(val throwable: Throwable) : MovieState()
        class Loading(val isLoading: Boolean) : MovieState()
        class LoadMoreProgress(val isLoading: Boolean) : MovieState()
    }

    sealed class FavouriteState {
        object OnDelete : FavouriteState()
        class OnLoad(val data: MutableList<Movie>) : FavouriteState()
        class OnError(val throwable: Throwable) : FavouriteState()
    }

    fun getCurrentPage() = page

    fun incrementPage() = page++

    fun resetPage() {
        page = 1
    }

    init {
        val subscribe = exclusionUseCase
            .getExcludedMovies()
            .thread(schedulers)
            .map { exclusionList = it.toMutableList() }
            .subscribe()
        disposable.add(subscribe)
    }

    fun searchMovie(query: String) {
        this.query = query
        val subscribe = searchUseCase
            .searchMovie(page, query)
            .thread(schedulers)
            .doOnSubscribe { updateState(Loading(true)) }
            .doOnComplete { updateState(Loading(false)) }
            .map {
                it.movies = filterMovies(it.movies)
                MovieState.Success(it)
            }
            .subscribe(this::updateState) {
                updateState(MovieState.Error(it))
            }
        disposable.add(subscribe)
    }

    fun loadMore() {
        val subscribe = searchUseCase
            .searchMovie(getCurrentPage(), query)
            .thread(schedulers)
            .doOnSubscribe { updateState(LoadMoreProgress(true)) }
            .doOnComplete { updateState(LoadMoreProgress(false)) }
            .map {
                it.movies = filterMovies(it.movies)
                MovieState.Success(it)
            }
            .subscribe(this::updateState) {
                updateState(MovieState.Error(it))
            }
        disposable.add(subscribe)
    }

    fun onListItemClick(movie: Movie) {
        router.toDetail(movie)
    }

    fun onFavouriteClick(movie: Movie) {
        val subscribe = favouritesUseCase
            .deleteFavourite(movie)
            .thread(schedulers)
            .map { FavouriteState.OnDelete }
            .subscribe(this::updateFavState) {
                updateFavState(FavouriteState.OnError(it))
            }
        disposable.add(subscribe)
    }

    fun getFavourites() {
        val subscribe = favouritesUseCase
            .getFavourites()
            .thread(schedulers)
            .map { OnLoad(filterMovies(it).toMutableList()) }
            .subscribe(this::updateFavState) {
                updateFavState(FavouriteState.OnError(it))
            }
        disposable.add(subscribe)
    }

    private fun filterMovies(result: List<Movie>): List<Movie> {
        return result.filterNot { exclusionList.contains(it.id) }
    }

    private fun updateState(state: MovieState) {
        mutableMovieLiveData.value = state
    }

    private fun updateFavState(state: FavouriteState) {
        mutableFavLiveData.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
