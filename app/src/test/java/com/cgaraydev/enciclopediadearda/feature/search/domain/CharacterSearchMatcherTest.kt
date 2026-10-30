package com.cgaraydev.enciclopediadearda.feature.search.domain

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterSearchMatcherTest {

    @Test
    fun matchesNamesRegardlessOfAccentsAndCase() {
        val character = character("one", "Fëanor")

        val results = CharacterSearchMatcher.search(
            listOf(character),
            "  FEANOR  "
        )

        assertEquals(listOf(character), results)
    }

    @Test
    fun ranksExactMatchBeforePrefixAndSubstring() {
        val characters = listOf(
            character("substring", "Historia de Arda"),
            character("prefix", "Arda antigua"),
            character("exact", "Arda")
        )

        val results = CharacterSearchMatcher.search(characters, "arda")

        assertEquals(
            listOf("exact", "prefix", "substring"),
            results.map { it.id }
        )
    }

    @Test
    fun matchesMultipleTermsInDifferentOrder() {
        val character = character("one", "Cronista de demostración")

        val results = CharacterSearchMatcher.search(
            listOf(character),
            "demostracion cronista"
        )

        assertEquals(listOf(character), results)
    }

    @Test
    fun ignoresQueriesShorterThanTwoCharacters() {
        assertTrue(
            CharacterSearchMatcher.search(
                listOf(character("one", "Arda")),
                " a "
            ).isEmpty()
        )
    }

    private fun character(id: String, name: String) = Character(
        id = id,
        name = name,
        imagePath = null,
        biographyHtml = null
    )
}