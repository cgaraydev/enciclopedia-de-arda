package com.cgaraydev.enciclopediadearda.feature.characters.data

import android.content.Context
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterAssetDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val decoder: CharacterCatalogDecoder
) {
    suspend fun loadCharacters(): List<Character> =
        withContext(Dispatchers.IO) {
            val content = context.assets
                .open("demo/characters_list.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            decoder.decode(content)
        }
}