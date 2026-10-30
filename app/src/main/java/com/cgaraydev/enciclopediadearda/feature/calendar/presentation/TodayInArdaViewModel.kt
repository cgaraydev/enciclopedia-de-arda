package com.cgaraydev.enciclopediadearda.feature.calendar.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.MonthDay
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TodayInArdaViewModel @Inject constructor(
    private val repository: CalendarRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodayInArdaUiState>(
        TodayInArdaUiState.Loading(LocalDate.now())
    )
    val uiState = _uiState.asStateFlow()

    private var requestedDate: LocalDate? = null
    private var loadJob: Job? = null

    init {
        refresh()
    }

    fun refresh(date: LocalDate = LocalDate.now()) {
        if (requestedDate == date) return
        startLoading(date)
    }

    fun retry() {
        if (_uiState.value is TodayInArdaUiState.Error) {
            startLoading(LocalDate.now())
        }
    }

    private fun startLoading(date: LocalDate) {
        loadJob?.cancel()
        requestedDate = date
        _uiState.value = TodayInArdaUiState.Loading(date)

        loadJob = viewModelScope.launch {
            try {
                val events = repository.getEvents(MonthDay.from(date))

                _uiState.value = TodayInArdaUiState.Content(
                    date = date,
                    events = events
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = TodayInArdaUiState.Error(date)
            }
        }
    }
}