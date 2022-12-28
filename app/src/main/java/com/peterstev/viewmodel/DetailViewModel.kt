package com.peterstev.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peterstev.domain.model.Movie
import com.peterstev.domain.usecase.ExclusionUseCase
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.util.RxSchedulers
import com.peterstev.util.thread
import com.peterstev.viewmodel.DetailViewModel.FavouriteState.OnFavouriteAdded
import com.peterstev.viewmodel.DetailViewModel.FavouriteState.OnFavouriteRemoved
import io.reactivex.rxjava3.disposables.CompositeDisposable
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val favouritesUseCase: FavouritesUseCase,
    private val exclusionUseCase: ExclusionUseCase,
    private val schedulers: RxSchedulers,
) : ViewModel() {

    private val mutableExclusionLiveData = MutableLiveData<ExclusionState>()
    val exclusionLiveData: LiveData<ExclusionState> = mutableExclusionLiveData

    private val mutableFavouriteLiveData = MutableLiveData<FavouriteState>()
    val favouriteLiveData: LiveData<FavouriteState> = mutableFavouriteLiveData

    sealed class ExclusionState {
        object OnExclude : ExclusionState()
        object OnInclude : ExclusionState()
    }

    sealed class FavouriteState {
        object OnFavouriteAdded : FavouriteState()
        object OnFavouriteRemoved : FavouriteState()
    }

    private val disposable by lazy { CompositeDisposable() }

    fun onExcludeClick(movie: Movie) {
        val subscribe = exclusionUseCase
            .excludeMovie(movie)
            .thread(schedulers)
            .map { ExclusionState.OnExclude }
            .subscribe(this::updateExclusionState) {

            }
        disposable.add(subscribe)
    }

    fun onIncludeClick(movie: Movie) {
        val subscribe = exclusionUseCase
            .includeMovie(movie)
            .thread(schedulers)
            .map { ExclusionState.OnInclude }
            .subscribe(this::updateExclusionState)
        disposable.add(subscribe)
    }

    fun getSingleMovie(movie: Movie) {
        val subscribe = favouritesUseCase
            .getSingleFavourite(movie.id)
            .thread(schedulers)
            .map {
                when {
                    it.isFavourite -> OnFavouriteAdded
                    else -> OnFavouriteRemoved
                }
            }
            .subscribe(this::updateFavouriteState) {
                updateFavouriteState(OnFavouriteRemoved)
            }
        disposable.add(subscribe)
    }

    fun onAddFavClick(movie: Movie) {
        movie.isFavourite = true
        val subscribe = favouritesUseCase
            .addFavourite(movie)
            .thread(schedulers)
            .map { OnFavouriteAdded }
            .subscribe(this::updateFavouriteState)
        disposable.add(subscribe)
    }

    fun onRemoveFavClick(movie: Movie) {
        movie.isFavourite = false
        val subscribe = favouritesUseCase
            .deleteFavourite(movie)
            .thread(schedulers)
            .map { OnFavouriteRemoved }
            .subscribe(this::updateFavouriteState)
        disposable.add(subscribe)
    }

    private fun updateExclusionState(state: ExclusionState) {
        mutableExclusionLiveData.value = state
    }

    private fun updateFavouriteState(state: FavouriteState) {
        mutableFavouriteLiveData.value = state
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
