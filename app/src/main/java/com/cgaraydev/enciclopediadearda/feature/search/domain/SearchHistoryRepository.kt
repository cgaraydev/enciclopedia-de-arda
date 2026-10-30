package com.cgaraydev.enciclopediadearda.feature.search.domain

import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {
    fun observeHistory(): Flow<List<String>>
    suspend fun record(query: String)
    suspend fun clear()
}