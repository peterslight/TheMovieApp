package com.peterstev.database.injection

import android.app.Application
import com.peterstev.database.dao.ExclusionDao
import com.peterstev.database.dao.MovieDao
import com.peterstev.database.database.MovieDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesMovieDatabase(application: Application): MovieDatabase {
        return MovieDatabase.getInstance(application.applicationContext)
    }

    @Singleton
    @Provides
    internal fun providesMovieDao(database: MovieDatabase): MovieDao {
        return database.movieDao()
    }

    @Singleton
    @Provides
    internal fun providesExclusionDao(database: MovieDatabase): ExclusionDao {
        return database.exclusionDao()
    }
}
