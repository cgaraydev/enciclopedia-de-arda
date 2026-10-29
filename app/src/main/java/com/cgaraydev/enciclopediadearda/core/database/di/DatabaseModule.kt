package com.cgaraydev.enciclopediadearda.core.database.di

import android.content.Context
import androidx.room.Room
import com.cgaraydev.enciclopediadearda.core.database.ArdaDatabase
import com.cgaraydev.enciclopediadearda.core.database.FavoriteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ArdaDatabase = Room.databaseBuilder(
        context,
        ArdaDatabase::class.java,
        "arda.db"
    ).build()

    @Provides
    fun provideFavoriteDao(
        database: ArdaDatabase
    ): FavoriteDao = database.favoriteDao()
}