package com.cgaraydev.enciclopediadearda.feature.search.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.cgaraydev.enciclopediadearda.feature.search.domain.CharacterSearchMatcher
import com.cgaraydev.enciclopediadearda.feature.search.domain.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataStoreSearchHistoryRepository(
    private val dataStore: DataStore<Preferences>
) : SearchHistoryRepository {

    private val historyKey = stringPreferencesKey("recent_queries")
    private val spaces = Regex("\\s+")

    override fun observeHistory(): Flow<List<String>> =
        dataStore.data
            .map { preferences ->
                decode(preferences[historyKey])
            }
            .distinctUntilChanged()

    override suspend fun record(query: String) {
        val displayQuery = query.trim().replace(spaces, " ")
        val normalizedQuery = CharacterSearchMatcher.normalize(displayQuery)
        if (normalizedQuery.length < 2) return

        dataStore.edit { preferences ->
            val previous = decode(preferences[historyKey])

            val updated = (
                    listOf(displayQuery) + previous.filterNot {
                        CharacterSearchMatcher.normalize(it) == normalizedQuery
                    }
                    ).take(10)

            preferences[historyKey] = Json.encodeToString(updated)
        }
    }

    override suspend fun clear() {
        dataStore.edit { preferences ->
            preferences.remove(historyKey)
        }
    }

    private fun decode(value: String?): List<String> =
        value?.let { Json.decodeFromString<List<String>>(it) }
            ?: emptyList()
}