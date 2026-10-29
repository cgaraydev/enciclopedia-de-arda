package com.cgaraydev.enciclopediadearda.feature.favorites.di

import com.cgaraydev.enciclopediadearda.feature.favorites.data.RoomFavoritesRepository
import com.cgaraydev.enciclopediadearda.feature.favorites.domain.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FavoritesModule {

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        implementation: RoomFavoritesRepository
    ): FavoritesRepository
}