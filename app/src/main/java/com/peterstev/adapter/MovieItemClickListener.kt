package com.peterstev.adapter

import com.peterstev.domain.model.Movie

interface MovieItemClickListener {
    fun loadMore()

    fun onItemClick(movie: Movie)

    fun onFavouriteClick(movie: Movie)
}
