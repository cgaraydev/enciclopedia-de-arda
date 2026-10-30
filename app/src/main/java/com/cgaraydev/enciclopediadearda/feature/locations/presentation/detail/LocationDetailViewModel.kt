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

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<LocationDetailUiState>(
            LocationDetailUiState.Loading
        )
    val uiState = _uiState.asStateFlow()

    private var currentId: String? = null
    private var loadJob: Job? = null

    fun loadLocation(id: String) {
        if (currentId == id) return

        currentId = id
        startLoading(id)
    }

    fun retry() {
        if (_uiState.value != LocationDetailUiState.Error) return

        currentId?.let { startLoading(it) }
    }

    private fun startLoading(id: String) {
        loadJob?.cancel()
        _uiState.value = LocationDetailUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                val location = repository.getLocationById(id)

                _uiState.value = if (location == null) {
                    LocationDetailUiState.NotFound
                } else {
                    LocationDetailUiState.Content(location)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = LocationDetailUiState.Error
            }
        }
    }
}