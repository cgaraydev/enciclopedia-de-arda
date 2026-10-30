package com.cgaraydev.enciclopediadearda.feature.calendar.domain

import java.time.YearMonth

fun calendarMonthDays(month: YearMonth): List<Int?> {
    val leadingSpaces = month.atDay(1).dayOfWeek.value - 1

    return List<Int?>(leadingSpaces) { null } +
            (1..month.lengthOfMonth()).map { it }
}