package com.peterstev.database.dao

import androidx.room.*
import com.peterstev.database.entity.ExclusionEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

@Dao
interface ExclusionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun exclude(entity: ExclusionEntity): Single<Long>

    @Delete
    fun include(entity: ExclusionEntity): Single<Int>

    @Query("SELECT * FROM exclusion_table")
    fun getExcludedMovies(): Flowable<List<ExclusionEntity>>

    @Query("DELETE FROM exclusion_table")
    fun resetExclusionList(): Completable

}
