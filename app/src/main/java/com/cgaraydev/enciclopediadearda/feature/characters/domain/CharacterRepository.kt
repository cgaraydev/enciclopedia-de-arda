package com.cgaraydev.enciclopediadearda.feature.characters.domain

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>

    suspend fun getCharacterById(id: String): Character?
}