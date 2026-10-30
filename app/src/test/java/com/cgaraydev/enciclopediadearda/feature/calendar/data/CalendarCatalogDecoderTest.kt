package com.cgaraydev.enciclopediadearda.feature.calendar.data

import java.time.DateTimeException
import java.time.MonthDay
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarCatalogDecoderTest {

    private val decoder = CalendarCatalogDecoder()

    @Test
    fun preservesDateAndHtmlLinks() {
        val event = decoder.decode(
            """
            [{
              "day": 30,
              "month": 10,
              "description": "<a href='character:demo-001'>Exploradora</a>"
            }]
            """.trimIndent()
        ).single()

        assertEquals(MonthDay.of(10, 30), event.date)
        assertNull(event.year)
        assertTrue(
            event.descriptionHtml.contains("character:demo-001")
        )
    }

    @Test
    fun acceptsFebruary29() {
        val event = decoder.decode(
            """
            [{
              "day": 29,
              "month": 2,
              "description": "Evento ficticio"
            }]
            """.trimIndent()
        ).single()

        assertEquals(MonthDay.of(2, 29), event.date)
    }

    @Test(expected = DateTimeException::class)
    fun rejectsInvalidDates() {
        decoder.decode(
            """
            [{
              "day": 31,
              "month": 2,
              "description": "Evento ficticio"
            }]
            """.trimIndent()
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun rejectsBlankDescriptions() {
        decoder.decode(
            """
            [{
              "day": 1,
              "month": 1,
              "description": "   "
            }]
            """.trimIndent()
        )
    }
}