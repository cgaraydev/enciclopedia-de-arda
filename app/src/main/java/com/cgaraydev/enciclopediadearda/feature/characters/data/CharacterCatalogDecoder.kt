package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CharacterCatalogDecoder @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun decode(content: String): List<Character> {
        val entries = json.decodeFromString<List<CharacterDto>>(content)

        require(entries.all { it.id.isNotBlank() && it.name.isNotBlank() }) {
            "El catálogo contiene personajes sin identificador o nombre."
        }

        require(entries.map { it.id }.toSet().size == entries.size) {
            "El catálogo contiene identificadores de personajes duplicados."
        }

        return entries.map { it.toDomain() }
    }
}