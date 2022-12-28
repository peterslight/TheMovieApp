package com.peterstev.database.transform

import com.peterstev.database.entity.ExclusionEntity
import com.peterstev.domain.model.Movie
import javax.inject.Inject

class ExclusionEntityMapper @Inject constructor() {

    fun transform(entity: List<ExclusionEntity>): List<Int> {
        return entity.map { it.id }
    }

    fun transform(movie: Movie): ExclusionEntity {
        return movie.run {
            ExclusionEntity(
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
}
