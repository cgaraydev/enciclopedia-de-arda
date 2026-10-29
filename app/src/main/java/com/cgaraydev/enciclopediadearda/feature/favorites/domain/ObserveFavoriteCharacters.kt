package com.cgaraydev.enciclopediadearda.feature.favorites.domain

import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import java.text.Collator
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class ObserveFavoriteCharacters @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val characterRepository: CharacterRepository
) {
    operator fun invoke(): Flow<List<Character>> =
        favoritesRepository.observeFavorites()
            .map { references ->
                references
                    .filter { it.type == ArticleType.CHARACTER }
                    .map { it.id }
                    .toSet()
            }
            .distinctUntilChanged()
            .map { ids ->
                if (ids.isEmpty()) {
                    emptyList()
                } else {
                    val collator = Collator.getInstance(
                        Locale.forLanguageTag("es")
                    )

                    characterRepository.getCharacters()
                        .filter { it.id in ids }
                        .sortedWith { first, second ->
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