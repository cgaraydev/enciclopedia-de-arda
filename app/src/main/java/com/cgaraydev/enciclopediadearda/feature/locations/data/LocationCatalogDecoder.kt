package com.cgaraydev.enciclopediadearda.feature.locations.data

import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import javax.inject.Inject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class LocationCatalogDecoder @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun decode(content: String): List<Location> {
        val entries = json.decodeFromString<List<LocationDto>>(content)

        require(entries.all { it.id.isNotBlank() && it.name.isNotBlank() }) {
            "El catálogo contiene lugares sin identificador o nombre."
        }

        require(entries.map { it.id }.toSet().size == entries.size) {
            "El catálogo contiene identificadores de lugares duplicados."
        }

        return entries.map { it.toDomain() }
    }
}