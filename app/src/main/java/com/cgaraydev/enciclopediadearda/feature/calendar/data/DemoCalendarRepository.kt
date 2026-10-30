package com.cgaraydev.enciclopediadearda.feature.calendar.data

import android.content.Context
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarEvent
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.MonthDay
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class DemoCalendarRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val decoder: CalendarCatalogDecoder
) : CalendarRepository {

    private val mutex = Mutex()
    private var cachedEvents: List<CalendarEvent>? = null

    override suspend fun getEvents(
        date: MonthDay
    ): List<CalendarEvent> = withContext(Dispatchers.IO) {
        val events = mutex.withLock {
            cachedEvents ?: context.assets
                .open("demo/calendar.json")
                .bufferedReader(Charsets.UTF_8)
                .use { reader ->
                    decoder.decode(reader.readText())
                }
                .also { cachedEvents = it }
        }

        events.filter { it.date == date }
    }
}