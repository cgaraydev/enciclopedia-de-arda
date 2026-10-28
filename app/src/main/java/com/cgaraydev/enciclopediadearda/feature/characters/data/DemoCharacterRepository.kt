package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DemoCharacterRepository @Inject constructor(
    private val dataSource: CharacterAssetDataSource
) : CharacterRepository {

    private val mutex = Mutex()
    private var cachedCharacters: List<Character>? = null

    override suspend fun getCharacters(): List<Character> =
        mutex.withLock {
            cachedCharacters
                ?: dataSource.loadCharacters().also {
                    cachedCharacters = it
                }
        }

    override suspend fun getCharacterById(id: String): Character? =
        getCharacters().firstOrNull { it.id == id }
}