package com.cgaraydev.enciclopediadearda.feature.search.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.search.domain.SearchHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchHistoryUiState(
    val entries: List<String> = emptyList(),
    val isLoading: Boolean = true,
    val loadFailed: Boolean = false,
    val updateFailed: Boolean = false,
    val isClearing: Boolean = false
)

@HiltViewModel
class SearchHistoryViewModel @Inject constructor(
    private val repository: SearchHistoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchHistoryUiState())
    val uiState = _uiState.asStateFlow()

    private var observationJob: Job? = null

    init {
        observeHistory()
    }

    fun retry() {
        if (_uiState.value.loadFailed) {
            observeHistory()
        }
    }

    fun record(query: String) {
        viewModelScope.launch {
            try {
                repository.record(query)
                _uiState.update { it.copy(updateFailed = false) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update { it.copy(updateFailed = true) }
            }
        }
    }

    fun clear() {
        if (_uiState.value.isClearing) return

        _uiState.update {
            it.copy(isClearing = true, updateFailed = false)
        }

        viewModelScope.launch {
            try {
                repository.clear()
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update { it.copy(updateFailed = true) }
            } finally {
                _uiState.update { it.copy(isClearing = false) }
            }
        }
    }

    private fun observeHistory() {
        observationJob?.cancel()
        _uiState.update {
            it.copy(isLoading = true, loadFailed = false)
        }

        observationJob = viewModelScope.launch {
            try {
                repository.observeHistory().collect { entries ->
                    _uiState.update {
                        it.copy(
                            entries = entries,
                            isLoading = false,
                            loadFailed = false
                        )
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, loadFailed = true)
                }
            }
        }
    }
}