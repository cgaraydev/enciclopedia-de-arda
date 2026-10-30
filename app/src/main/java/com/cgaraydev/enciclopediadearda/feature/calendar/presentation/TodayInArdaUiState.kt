package com.cgaraydev.enciclopediadearda.feature.calendar.presentation

import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarEvent
import java.time.LocalDate

sealed interface TodayInArdaUiState {
    val date: LocalDate

    data class Loading(
        override val date: LocalDate
    ) : TodayInArdaUiState

    data class Content(
        override val date: LocalDate,
        val events: List<CalendarEvent>
    ) : TodayInArdaUiState

    data class Error(
        override val date: LocalDate
    ) : TodayInArdaUiState
}