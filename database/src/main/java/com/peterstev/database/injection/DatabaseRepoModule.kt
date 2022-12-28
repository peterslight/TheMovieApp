package com.peterstev.database.injection

import com.peterstev.database.inversion.ExclusionRepositoryImpl
import com.peterstev.database.inversion.FavouriteRepositoryImpl
import com.peterstev.domain.repository.ExclusionRepository
import com.peterstev.domain.repository.FavouriteRepository
import dagger.Binds
import dagger.Module

@Module
abstract class DatabaseRepoModule {

    @Binds
    internal abstract fun bindFavouriteRepository(
        repositoryImpl: FavouriteRepositoryImpl,
    ): FavouriteRepository

    @Binds
    internal abstract fun bindExclusionRepository(
        exclusionRepository: ExclusionRepositoryImpl,
    ): ExclusionRepository
}
