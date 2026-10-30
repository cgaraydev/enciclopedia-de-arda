package com.cgaraydev.enciclopediadearda.feature.locations.di

import com.cgaraydev.enciclopediadearda.feature.locations.data.DemoLocationRepository
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationRepository(
        implementation: DemoLocationRepository
    ): LocationRepository
}