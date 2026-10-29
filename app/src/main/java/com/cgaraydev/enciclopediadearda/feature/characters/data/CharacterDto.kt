package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import kotlinx.serialization.Serializable
import com.cgaraydev.enciclopediadearda.core.model.CatalogImage

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
    val images: List<CharacterImageDto>? = null,
    val wikiUrl: WikiUrlDto? = null
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
        images = images.orEmpty().map { image ->
            CatalogImage(
                imagePath = image.url,
                artistHtml = image.artist
            )
        },
        externalLinks = wikiUrl?.toDomain().orEmpty()
    )
}

@Serializable
internal data class CharacterImageDto(
    val url: String? = null,
    val artist: String? = null
)