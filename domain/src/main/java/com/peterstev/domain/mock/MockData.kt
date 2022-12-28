package com.peterstev.domain.mock

import com.peterstev.domain.model.Movie
import com.peterstev.domain.model.MovieResult

class MockData {

    companion object {
        val movie = Movie(
            adult = false, backdropPath = "path",
            id = 1234, originalLanguage = "english",
            originalTitle = "title", overview = "overview",
            popularity = 1.5, posterPath = "postPath",
            releaseDate = "monday", title = "title",
            video = false, voteAverage = 1.1, voteCount = 4,
        )

        val movieResult = MovieResult(
            movies = listOf(movie, movie, movie),
            page = 1,
            totalPages = 5,
            totalResults = 10
        )

    }
}
