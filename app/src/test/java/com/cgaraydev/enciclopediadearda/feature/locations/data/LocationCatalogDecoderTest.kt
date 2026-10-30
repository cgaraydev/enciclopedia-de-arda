package com.cgaraydev.enciclopediadearda.feature.locations.data

import com.cgaraydev.enciclopediadearda.core.model.CatalogLinkSource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationCatalogDecoderTest {

    private val decoder = LocationCatalogDecoder()

    @Test
    fun preservesHtmlAndLocationFields() {
        val history =
            "<p>Visita <a href='location:other'>otro lugar</a>.</p>"

        val location = decoder.decode(
            """
            [{
              "id": "archive",
              "name": "Archivo",
              "capital": "Capital de prueba",
              "governance": "Consejo",
              "history": "$history",
              "type": "Archivo",
              "tags": ["other_locations"]
            }]
            """.trimIndent()
        ).single()

        assertEquals(history, location.historyHtml)
        assertEquals("Capital de prueba", location.capital)
        assertEquals("Consejo", location.governance)
        assertEquals("Archivo", location.typeHtml)
        assertEquals(listOf("other_locations"), location.tags)
    }

    @Test
    fun acceptsMissingAndNullOptionalFields() {
        val locations = decoder.decode(
            """
            [
              {"id": "one", "name": "Uno"},
              {
                "id": "two",
                "name": "Dos",
                "history": null,
                "images": null,
                "wikiUrl": null,
                "tags": null
              }
            ]
            """.trimIndent()
        )

        assertTrue(locations.all { it.images.isEmpty() })
        assertTrue(locations.all { it.externalLinks.isEmpty() })
        assertTrue(locations.all { it.tags.isEmpty() })
        assertTrue(locations.all { it.historyHtml == null })
        assertNull(locations.first().imagePath)
    }

    @Test
    fun mapsImagesAndFiltersExternalLinks() {
        val location = decoder.decode(
            """
            [{
              "id": "archive",
              "name": "Archivo",
              "images": [{
                "url": "demo/image.webp",
                "artist": "<b>Autor de prueba</b>"
              }],
              "wikiUrl": {
                "website": "https://example.org",
                "wikipedia": "javascript:alert(1)"
              },
              "futureField": "ignored"
            }]
            """.trimIndent()
        ).single()

        assertEquals("demo/image.webp", location.images.single().imagePath)
        assertEquals(
            "<b>Autor de prueba</b>",
            location.images.single().artistHtml
        )
        assertEquals(
            CatalogLinkSource.WEBSITE,
            location.externalLinks.single().source
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsDuplicateIds() {
        decoder.decode(
            """
            [
              {"id": "same", "name": "Uno"},
              {"id": "same", "name": "Dos"}
            ]
            """.trimIndent()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsBlankNames() {
        decoder.decode("""[{"id":"one","name":" "}]""")
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsBlankIds() {
        decoder.decode("""[{"id":" ","name":"Uno"}]""")
    }
}