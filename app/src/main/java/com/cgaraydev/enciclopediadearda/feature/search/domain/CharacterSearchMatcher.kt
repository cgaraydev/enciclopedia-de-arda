package com.cgaraydev.enciclopediadearda.feature.search.domain

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import java.text.Collator
import java.text.Normalizer
import java.util.Locale

object CharacterSearchMatcher {

    private val marks = Regex("\\p{M}+")
    private val spaces = Regex("\\s+")

    fun normalize(value: String): String =
        Normalizer.normalize(value, Normalizer.Form.NFD)
            .replace(marks, "")
            .lowercase(Locale.ROOT)
            .trim()
            .replace(spaces, " ")

    fun search(
        characters: List<Character>,
        query: String
    ): List<Character> {
        val normalizedQuery = normalize(query)
        if (normalizedQuery.length < 2) return emptyList()

        val terms = normalizedQuery.split(" ")
        val collator = Collator.getInstance(
            Locale.forLanguageTag("es")
        )

        return characters.mapNotNull { character ->
            val name = normalize(character.name)

            val score = when {
                name == normalizedQuery -> 4
                name.startsWith(normalizedQuery) -> 3
                name.contains(normalizedQuery) -> 2
                terms.all { name.contains(it) } -> 1
                else -> 0
            }

            if (score == 0) {
                null
            } else {
                RankedCharacter(character, score)
            }
        }.sortedWith { first, second ->
            val scoreOrder = second.score.compareTo(first.score)

            if (scoreOrder != 0) {
                scoreOrder
            } else {
                collator.compare(
                    first.character.name,
                    second.character.name
                )
            }
        }.take(50).map { it.character }
    }

    private data class RankedCharacter(
        val character: Character,
        val score: Int
    )
}