package com.cgaraydev.enciclopediadearda.feature.calendar.domain

import java.time.MonthDay

interface CalendarRepository {
    suspend fun getEvents(date: MonthDay): List<CalendarEvent>
}