package com.cgaraydev.enciclopediadearda.feature.calendar.domain

import java.time.YearMonth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalendarMonthDaysTest {

    @Test
    fun startsWeeksOnMonday() {
        // Junio de 2026 comienza en lunes.
        val days = calendarMonthDays(YearMonth.of(2026, 6))

        assertEquals(1, days.first())
        assertEquals(30, days.filterNotNull().size)
    }

    @Test
    fun insertsLeadingSpacesBeforeFirstDay() {
        // Octubre de 2026 comienza en jueves.
        val days = calendarMonthDays(YearMonth.of(2026, 10))

        assertTrue(days.take(3).all { it == null })
        assertEquals(1, days[3])
        assertEquals(31, days.last())
    }

    @Test
    fun februaryHas28DaysInCommonYear() {
        val days = calendarMonthDays(YearMonth.of(2026, 2))

        assertEquals((1..28).toList(), days.filterNotNull())
    }

    @Test
    fun februaryHas29DaysInLeapYear() {
        val days = calendarMonthDays(YearMonth.of(2028, 2))

        assertEquals((1..29).toList(), days.filterNotNull())
    }
}