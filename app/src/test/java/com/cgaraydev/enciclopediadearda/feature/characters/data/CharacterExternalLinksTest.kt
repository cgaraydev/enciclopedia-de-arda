package com.cgaraydev.enciclopediadearda.feature.characters.data

import com.cgaraydev.enciclopediadearda.core.model.CatalogExternalLink
import com.cgaraydev.enciclopediadearda.core.model.CatalogLinkSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterExternalLinksTest {

    private val decoder = CharacterCatalogDecoder()

    @Test
    fun `keeps valid links and excludes empty or unsupported URLs`() {
        val character = decoder.decode(
            """
            [{
              "id": "demo",
              "name": "Demo",
              "wikiUrl": {
                "tolkienGateway": "   ",
                "lotrFandom": "javascript:alert(1)",
                "lotrFandomEs": "/wiki/Demo",
                "wikipedia": " https://example.org/wiki/Demo ",
                "website": "https://"
              }
            }]
            """.trimIndent()
        ).single()

        assertEquals(
            listOf(
                CatalogExternalLink(
                    source = CatalogLinkSource.WIKIPEDIA,
                    url = "https://example.org/wiki/Demo"
                )
            ),
            character.externalLinks
        )
    }

    @Test
    fun `supports articles with missing or null wiki links`() {
        val characters = decoder.decode(
            """
            [
              {"id": "one", "name": "One"},
              {"id": "two", "name": "Two", "wikiUrl": null}
            ]
            """.trimIndent()
        )

        assertTrue(characters.all { it.externalLinks.isEmpty() })
    }
}