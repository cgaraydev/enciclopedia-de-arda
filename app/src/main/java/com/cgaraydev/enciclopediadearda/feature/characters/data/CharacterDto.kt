package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.core.data.catalog.CatalogImageDto
import com.cgaraydev.enciclopediadearda.core.data.catalog.WikiUrlDto
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterDto(
    val id: String,
    val name: String,
    val poster: String? = null,
    val biography: String? = null,
    val otherNames: String? = null,
    val birth: String? = null,
    val death: String? = null,
    val titles: String? = null,
    val love: String? = null,
    val family: String? = null,
    val house: String? = null,
    val etymology: String? = null,
    val images: List<CatalogImageDto>? = null,
    val wikiUrl: WikiUrlDto? = null,
    val tags: List<String>? = null
) {
    fun toDomain(): Character = Character(
        id = id,
        name = name,
        imagePath = poster,
        biographyHtml = biography,
        otherNames = otherNames,
        birth = birth,
        death = death,
        titles = titles,
        love = love,
        family = family,
        house = house,
        etymologyHtml = etymology,
        images = images.orEmpty().map { it.toDomain() },
        externalLinks = wikiUrl?.toDomain().orEmpty(),
        tags = tags.orEmpty()
    )
}