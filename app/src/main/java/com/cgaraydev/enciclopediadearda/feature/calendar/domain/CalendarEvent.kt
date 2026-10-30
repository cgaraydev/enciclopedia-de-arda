package com.cgaraydev.enciclopediadearda.feature.calendar.domain

import java.time.MonthDay

data class CalendarEvent(
    val date: MonthDay,
    val year: String?,
    val descriptionHtml: String
)