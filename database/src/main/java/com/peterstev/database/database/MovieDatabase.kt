package com.peterstev.database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.peterstev.database.dao.ExclusionDao
import com.peterstev.database.dao.MovieDao
import com.peterstev.database.entity.ExclusionEntity
import com.peterstev.database.entity.MovieEntity

@Database(
    entities = [MovieEntity::class, ExclusionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao
    abstract fun exclusionDao(): ExclusionDao

    companion object {
        private const val databaseName = "movie_database"

        @Volatile
        private var database: MovieDatabase? = null

        fun getInstance(context: Context) =
            database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MovieDatabase::class.java,
                    databaseName
                ).build()
                database = instance
                instance
            }
    }
}
