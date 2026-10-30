package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object CharactersDestination : NavKey

@Serializable
data class CharacterDetailDestination(
    val characterId: String
) : NavKey

@Serializable
data object FavoritesDestination : NavKey

@Serializable
data object HomeDestination : NavKey

@Serializable
data object SearchDestination : NavKey

@Serializable
data object CalendarDestination : NavKey

@Serializable
data object LocationsDestination : NavKey

@Serializable
data class LocationDetailDestination(
    val locationId: String
) : NavKey