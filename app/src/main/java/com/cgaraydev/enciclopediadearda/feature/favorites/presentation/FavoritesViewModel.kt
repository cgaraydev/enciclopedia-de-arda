package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.favorites.domain.ObserveFavoriteCharacters
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val observeFavoriteCharacters: ObserveFavoriteCharacters
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var observationJob: Job? = null

    init {
        startObserving()
    }

    fun retry() {
        if (_uiState.value == FavoritesUiState.Error) {
            startObserving()
        }
    }

    private fun startObserving() {
        observationJob?.cancel()
        _uiState.value = FavoritesUiState.Loading

        observationJob = viewModelScope.launch {
            try {
                observeFavoriteCharacters().collect { characters ->
                    _uiState.value = FavoritesUiState.Content(characters)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = FavoritesUiState.Error
            }
        }
    }
}