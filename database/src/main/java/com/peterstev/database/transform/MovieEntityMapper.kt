package com.peterstev.database.transform

import com.peterstev.database.entity.MovieEntity
import com.peterstev.domain.model.Movie
import javax.inject.Inject

class MovieEntityMapper @Inject constructor() {

    fun transform(entity: MovieEntity): Movie {
        return entity.run {
            transformEntityToMovie(this)
        }
    }

    fun transform(entity: List<MovieEntity>): List<Movie> {
        return entity.map {
            it.run {
                transformEntityToMovie(it)
            }
        }
    }

    fun transform(movie: Movie): MovieEntity {
        return movie.run {
            MovieEntity(
                adult = adult,
                backdrop_path = backdropPath,
                id = id,
                original_language = originalLanguage,
                original_title = originalTitle,
                overview = overview,
                popularity = popularity,
                poster_path = posterPath,
                release_date = releaseDate,
                title = title,
                video = video,
                vote_average = voteAverage,
                vote_count = voteCount,
                is_favourite = isFavourite
            )
        }
    }

    private fun transformEntityToMovie(entity: MovieEntity): Movie {
        return entity.run {
            Movie(
                adult = adult,
                backdropPath = backdrop_path,
                id = id,
                originalLanguage = original_language,
                originalTitle = original_title,
                overview = overview,
                popularity = popularity,
                posterPath = poster_path,
                releaseDate = release_date,
                title = title,
                video = video,
                voteAverage = vote_average,
                voteCount = vote_count,
                isFavourite = is_favourite
            )
        }
    }
}
