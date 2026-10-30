package com.cgaraydev.enciclopediadearda.feature.favorites.domain

import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import java.text.Collator
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ObserveFavoriteArticles @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) {
    operator fun invoke(): Flow<List<FavoriteArticle>> =
        favoritesRepository.observeFavorites()
            .map { it.toSet() }
            .distinctUntilChanged()
            .map { references ->
                val characterIds = references
                    .filter { it.type == ArticleType.CHARACTER }
                    .map { it.id }
                    .toSet()

                val locationIds = references
                    .filter { it.type == ArticleType.LOCATION }
                    .map { it.id }
                    .toSet()

                val characters = if (characterIds.isEmpty()) {
                    emptyList()
                } else {
                    characterRepository.getCharacters()
                        .filter { it.id in characterIds }
                        .map {
                            FavoriteArticle(
                                type = ArticleType.CHARACTER,
                                id = it.id,
                                name = it.name
                            )
                        }
                }

                val locations = if (locationIds.isEmpty()) {
                    emptyList()
                } else {
                    locationRepository.getLocations()
                        .filter { it.id in locationIds }
                        .map {
                            FavoriteArticle(
                                type = ArticleType.LOCATION,
                                id = it.id,
                                name = it.name
                            )
                        }
                }

                val collator = Collator.getInstance(
                    Locale.forLanguageTag("es")
                )

                (characters + locations).sortedWith { first, second ->
                    val categoryOrder =
                        first.type.ordinal.compareTo(second.type.ordinal)

                    if (categoryOrder != 0) {
                        categoryOrder
                    } else {
                        val nameOrder = collator.compare(
                            first.name,
                            second.name
                        )

                        if (nameOrder != 0) {
                            nameOrder
                        } else {
                            first.id.compareTo(second.id)
                        }
                    }
                }
            }
            .flowOn(Dispatchers.Default)
}