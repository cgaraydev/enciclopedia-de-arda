package com.cgaraydev.enciclopediadearda.feature.locations.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LocationsUiState {
    data object Loading : LocationsUiState
    data object Error : LocationsUiState
    data class Content(val locations: List<Location>) : LocationsUiState
}

@HiltViewModel
class LocationsViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LocationsUiState>(LocationsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadLocations()
    }

    fun retry() {
        if (_uiState.value == LocationsUiState.Error) {
            loadLocations()
        }
    }

    private fun loadLocations() {
        if (loadJob?.isActive == true) return

        _uiState.value = LocationsUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                _uiState.value = LocationsUiState.Content(
                    repository.getLocations()
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = LocationsUiState.Error
            }
        }
    }
}