package com.peterstev.data.mapper

import com.peterstev.data.models.JsonMovie
import com.peterstev.data.models.JsonMovieResult
import com.peterstev.data.util.IMAGE_BASE_URL_500
import com.peterstev.data.util.IMAGE_BASE_URL_ORIGINAL
import com.peterstev.domain.model.Movie
import com.peterstev.domain.model.MovieResult
import javax.inject.Inject

class MovieMapper @Inject constructor() {

    fun transform(data: JsonMovieResult): MovieResult {
        return data.run {
            MovieResult(
                page = page,
                totalPages = total_pages,
                totalResults = total_results,
                movies = unpack(results)
            )
        }
    }

    private fun unpack(items: List<JsonMovie>): List<Movie> {
        return items.map { item ->
            item.run {
                Movie(
                    adult = adult,
                    backdropPath = backdrop_path?.let { IMAGE_BASE_URL_ORIGINAL + it } ?: "",
                    id = id,
                    originalLanguage = original_language ?: "",
                    originalTitle = original_title ?: "",
                    overview = overview ?: "",
                    popularity = popularity ?: 0.0,
                    posterPath = poster_path?.let { IMAGE_BASE_URL_500 + it } ?: "",
                    releaseDate = release_date ?: "",
                    title = title ?: "",
                    video = video ?: false,
                    voteAverage = vote_average ?: 0.0,
                    voteCount = vote_count ?: 0
                )
            }
        }
    }
}
