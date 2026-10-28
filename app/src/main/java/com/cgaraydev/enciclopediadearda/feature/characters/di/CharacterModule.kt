package com.cgaraydev.enciclopediadearda.feature.characters.di

import com.cgaraydev.enciclopediadearda.feature.characters.data.DemoCharacterRepository
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CharacterModule {

    @Binds
    @Singleton
    abstract fun bindCharacterRepository(
        implementation: DemoCharacterRepository
    ): CharacterRepository
}