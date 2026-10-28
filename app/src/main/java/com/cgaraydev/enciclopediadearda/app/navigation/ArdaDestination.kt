package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CharactersDestination : NavKey

@Serializable
data class CharacterDetailDestination(
    val characterId: String
) : NavKey