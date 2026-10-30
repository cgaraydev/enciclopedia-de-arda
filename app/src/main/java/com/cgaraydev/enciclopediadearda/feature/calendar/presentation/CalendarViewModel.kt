package com.cgaraydev.enciclopediadearda.feature.calendar.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.MonthDay
import java.time.YearMonth
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarUiState(
    val month: YearMonth,
    val selectedDay: Int?,
    val events: TodayInArdaUiState? = null
)

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: CalendarRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val today = LocalDate.now()

    private val initialMonth = YearMonth.of(
        savedStateHandle.get<Int>(YEAR_KEY) ?: today.year,
        savedStateHandle.get<Int>(MONTH_KEY) ?: today.monthValue
    )

    private val initialDay = (
            savedStateHandle.get<Int>(DAY_KEY) ?: today.dayOfMonth
            ).takeIf { it in 1..initialMonth.lengthOfMonth() }

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            month = initialMonth,
            selectedDay = initialDay
        )
    )
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        initialDay?.let { loadEvents(initialMonth.atDay(it)) }
    }

    fun selectMonth(month: Int) {
        require(month in 1..12)

        val current = _uiState.value
        if (current.month.monthValue == month) return

        loadJob?.cancel()

        val selectedMonth = YearMonth.of(current.month.year, month)

        savedStateHandle[YEAR_KEY] = selectedMonth.year
        savedStateHandle[MONTH_KEY] = month
        savedStateHandle[DAY_KEY] = 0

        _uiState.value = CalendarUiState(
            month = selectedMonth,
            selectedDay = null
        )
    }

    fun selectDay(day: Int) {
        val current = _uiState.value
        require(day in 1..current.month.lengthOfMonth())

        if (current.selectedDay == day) return

        savedStateHandle[DAY_KEY] = day
        loadEvents(current.month.atDay(day))
    }

    fun retry() {
        val current = _uiState.value
        if (current.events !is TodayInArdaUiState.Error) return

        current.selectedDay?.let {
            loadEvents(current.month.atDay(it))
        }
    }

    private fun loadEvents(date: LocalDate) {
        loadJob?.cancel()

        _uiState.update {
            it.copy(
                selectedDay = date.dayOfMonth,
                events = TodayInArdaUiState.Loading(date)
            )
        }

        loadJob = viewModelScope.launch {
            try {
                val events = repository.getEvents(MonthDay.from(date))

                _uiState.update {
                    it.copy(
                        events = TodayInArdaUiState.Content(
                            date = date,
                            events = events
                        )
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(events = TodayInArdaUiState.Error(date))
                }
            }
        }
    }

    private companion object {
        const val YEAR_KEY = "calendar_year"
        const val MONTH_KEY = "calendar_month"
        const val DAY_KEY = "calendar_day"
    }
}