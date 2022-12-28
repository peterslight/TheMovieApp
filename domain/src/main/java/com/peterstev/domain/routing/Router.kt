package com.peterstev.domain.routing

import com.peterstev.domain.model.Movie

interface Router {

    fun toDetail(movie: Movie)
}
