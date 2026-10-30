package com.cgaraydev.enciclopediadearda.feature.calendar.data

import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarEvent
import java.time.MonthDay
import javax.inject.Inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CalendarCatalogDecoder @Inject constructor() {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun decode(content: String): List<CalendarEvent> {
        val entries = json.decodeFromString<List<CalendarEventDto>>(content)

        return entries.map { entry ->
            require(entry.description.isNotBlank()) {
                "El calendario contiene una descripción vacía."
            }

            CalendarEvent(
                date = MonthDay.of(entry.month, entry.day),
                year = entry.year?.trim()?.takeIf { it.isNotEmpty() },
                descriptionHtml = entry.description
            )
        }
    }
}

@Serializable
internal data class CalendarEventDto(
    val day: Int,
    val month: Int,
    val year: String? = null,
    val description: String
)