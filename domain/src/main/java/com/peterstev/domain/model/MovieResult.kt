package com.peterstev.domain.model

import java.io.Serializable

data class MovieResult(
    var movies: List<Movie>,
    val page: Int,
    val totalPages: Int,
    val totalResults: Int,
) : Serializable
