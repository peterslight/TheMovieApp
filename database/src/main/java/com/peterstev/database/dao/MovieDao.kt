package com.peterstev.database.dao

import androidx.room.*
import com.peterstev.database.entity.MovieEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFavourite(entity: MovieEntity): Single<Long>

    @Query("SELECT * FROM movie_table")
    fun getFavourites(): Flowable<List<MovieEntity>>

    @Query("SELECT * FROM movie_table WHERE id = :id")
    fun getSingleFavourite(id: Int): Single<MovieEntity>

    @Delete
    fun deleteFavourite(entity: MovieEntity): Single<Int>

    @Query("DELETE FROM movie_table")
    fun deleteAllFavourites(): Completable
}
