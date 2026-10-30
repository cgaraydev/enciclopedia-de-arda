package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailUiState
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState<Location>>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentId: String? = null
    private var loadJob: Job? = null

    fun loadLocation(id: String) {
        if (currentId == id) return

        currentId = id
        startLoading(id)
    }

    fun retry() {
        if (_uiState.value != DetailUiState.Error) return

        currentId?.let { startLoading(it) }
    }

    private fun startLoading(id: String) {
        loadJob?.cancel()
        _uiState.value = DetailUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                val location = repository.getLocationById(id)

                _uiState.value = if (location == null) {
                    DetailUiState.NotFound
                } else {
                    DetailUiState.Content(location)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = DetailUiState.Error
            }
        }
    }
}