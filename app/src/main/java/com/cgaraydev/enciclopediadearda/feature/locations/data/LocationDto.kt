package com.cgaraydev.enciclopediadearda.feature.locations.data

import com.cgaraydev.enciclopediadearda.core.data.catalog.CatalogImageDto
import com.cgaraydev.enciclopediadearda.core.data.catalog.WikiUrlDto
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import kotlinx.serialization.Serializable

@Serializable
internal data class LocationDto(
    val id: String,
    val name: String,
    val poster: String? = null,
    val capital: String? = null,
    val inhabitants: String? = null,
    val governance: String? = null,
    val languages: String? = null,
    val founded: String? = null,
    val destroyed: String? = null,
    val otherNames: String? = null,
    val type: String? = null,
    val events: String? = null,
    val location: String? = null,
    val history: String? = null,
    val majorTowns: String? = null,
    val regions: String? = null,
    val etymology: String? = null,
    val images: List<CatalogImageDto>? = null,
    val wikiUrl: WikiUrlDto? = null,
    val tags: List<String>? = null
) {
    fun toDomain(): Location = Location(
        id = id,
        name = name,
        imagePath = poster,
        capital = capital,
        inhabitants = inhabitants,
        governance = governance,
        languages = languages,
        founded = founded,
        destroyed = destroyed,
        otherNames = otherNames,
        typeHtml = type,
        events = events,
        location = location,
        historyHtml = history,
        majorTowns = majorTowns,
        regions = regions,
        etymologyHtml = etymology,
        images = images.orEmpty().map { it.toDomain() },
        externalLinks = wikiUrl?.toDomain().orEmpty(),
        tags = tags.orEmpty()
    )
}