package com.cgaraydev.enciclopediadearda.feature.search.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.cgaraydev.enciclopediadearda.feature.search.data.DataStoreSearchHistoryRepository
import com.cgaraydev.enciclopediadearda.feature.search.domain.SearchHistoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.searchHistoryDataStore by preferencesDataStore(
    name = "search_history"
)

@Module
@InstallIn(SingletonComponent::class)
object SearchHistoryModule {

    @Provides
    @Singleton
    fun provideSearchHistoryRepository(
        @ApplicationContext context: Context
    ): SearchHistoryRepository =
        DataStoreSearchHistoryRepository(context.searchHistoryDataStore)
}