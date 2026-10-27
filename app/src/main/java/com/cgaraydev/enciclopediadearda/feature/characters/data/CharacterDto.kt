package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterDto(
    val id: String,
    val name: String,
    val poster: String? = null,
    val biography: String? = null
) {
    fun toDomain(): Character = Character(
        id = id,
        name = name,
        imagePath = poster,
        biographyHtml = biography
    )
}