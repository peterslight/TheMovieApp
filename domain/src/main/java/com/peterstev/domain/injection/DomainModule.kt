package com.peterstev.domain.injection

import com.peterstev.domain.inversion.ExclusionUseCaseImpl
import com.peterstev.domain.inversion.FavouritesUseCaseImpl
import com.peterstev.domain.inversion.SearchUseCaseImpl
import com.peterstev.domain.usecase.ExclusionUseCase
import com.peterstev.domain.usecase.FavouritesUseCase
import com.peterstev.domain.usecase.SearchUseCase
import dagger.Binds
import dagger.Module

@Module
abstract class DomainModule {

    @Binds
    internal abstract fun bindBaseUseCase(
        useCaseImpl: SearchUseCaseImpl,
    ): SearchUseCase

    @Binds
    internal abstract fun bindFavouriteUseCase(
        favouritesUseCaseImpl: FavouritesUseCaseImpl,
    ): FavouritesUseCase

    @Binds
    internal abstract fun bindExclusionUseCase(
        exclusionUseCaseImpl: ExclusionUseCaseImpl,
    ): ExclusionUseCase
}
