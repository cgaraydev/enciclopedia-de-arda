package com.cgaraydev.enciclopediadearda.core.domain.links

import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.core.model.CatalogLink
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CatalogLinkParserTest {

    @Test
    fun parsesEveryRegisteredArticleType() {
        ArticleType.entries.forEach { type ->
            assertEquals(
                CatalogLink.Article(
                    ArticleReference(type, "demo-001")
                ),
                CatalogLinkParser.parse("${type.value}:demo-001")
            )
        }
    }

    @Test
    fun preservesCategoryWhenIdsMatch() {
        assertEquals(
            CatalogLink.Article(
                ArticleReference(ArticleType.CHARACTER, "same-id")
            ),
            CatalogLinkParser.parse("character:same-id")
        )

        assertEquals(
            CatalogLink.Article(
                ArticleReference(ArticleType.LOCATION, "same-id")
            ),
            CatalogLinkParser.parse("location:same-id")
        )
    }

    @Test
    fun acceptsOuterWhitespaceAndCaseInsensitiveScheme() {
        assertEquals(
            CatalogLink.Article(
                ArticleReference(ArticleType.LOCATION, "demo-001")
            ),
            CatalogLinkParser.parse("  LOCATION:demo-001  ")
        )
    }

    @Test
    fun preservesExternalWebLinks() {
        val url = "https://example.org/article?q=arda#history"

        assertEquals(
            CatalogLink.External(url),
            CatalogLinkParser.parse(url)
        )
    }

    @Test
    fun rejectsInvalidAndUnsupportedLinks() {
        listOf(
            "",
            " ",
            "character:",
            "location://demo-001",
            "character:demo-001#fragment",
            "unknown:demo-001",
            "javascript:alert(1)",
            "https://",
            "/relative/path"
        ).forEach { value ->
            assertNull(value, CatalogLinkParser.parse(value))
        }
    }
}