package com.cgaraydev.enciclopediadearda

import com.cgaraydev.enciclopediadearda.feature.characters.data.CharacterCatalogDecoder
import kotlinx.serialization.SerializationException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Test

class CharacterCatalogDecoderTest {

    private val decoder = CharacterCatalogDecoder()

    @Test
    fun `preserves HTML links and accepts optional fields`() {
        val content = """
            [
              {
                "id": "demo-001",
                "name": "Exploradora",
                "biography": "<a href='character:demo-002'>Cronista</a>",
                "titles": "Campo todavía no utilizado"
              }
            ]
        """.trimIndent()

        val character = decoder.decode(content).single()

        assertEquals("demo-001", character.id)
        assertEquals(
            "<a href='character:demo-002'>Cronista</a>",
            character.biographyHtml
        )
        assertNull(character.imagePath)
    }

    @Test
    fun `rejects entries without an identifier`() {
        assertThrows(SerializationException::class.java) {
            decoder.decode("""[{"name":"Exploradora"}]""")
        }
    }

    @Test
    fun `rejects duplicate identifiers`() {
        val content = """
            [
              {"id":"demo-001","name":"Exploradora"},
              {"id":"demo-001","name":"Cronista"}
            ]
        """.trimIndent()

        assertThrows(IllegalArgumentException::class.java) {
            decoder.decode(content)
        }
    }

    @Test
    fun `rejects blank identifiers`() {
        assertThrows(IllegalArgumentException::class.java) {
            decoder.decode("""[{"id":" ","name":"Exploradora"}]""")
        }
    }
}