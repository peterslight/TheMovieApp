package com.peterstev.data.models

data class JsonMovieResult(
    val page: Int,
    val results: List<JsonMovie>,
    val total_pages: Int,
    val total_results: Int
)
