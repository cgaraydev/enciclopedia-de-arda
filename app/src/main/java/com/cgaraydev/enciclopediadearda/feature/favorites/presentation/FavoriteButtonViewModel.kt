package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.favorites.domain.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoriteButtonUiState(
    val isFavorite: Boolean? = null,
    val isSaving: Boolean = false,
    val hasError: Boolean = false
)

@HiltViewModel
class FavoriteButtonViewModel @Inject constructor(
    private val repository: FavoritesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteButtonUiState())
    val uiState = _uiState.asStateFlow()

    private var currentKey: Pair<ArticleType, String>? = null
    private var observationJob: Job? = null
    private var saveJob: Job? = null

    fun load(type: ArticleType, id: String) {
        val key = type to id
        if (currentKey == key) return

        saveJob?.cancel()
        currentKey = key
        startObserving()
    }

    fun retry() {
        if (_uiState.value.isFavorite == null) {
            startObserving()
        } else {
            toggleFavorite()
        }
    }

    fun toggleFavorite() {
        val key = currentKey ?: return
        val state = _uiState.value
        val currentValue = state.isFavorite ?: return
        if (state.isSaving) return

        val desiredValue = !currentValue
        _uiState.value = state.copy(
            isSaving = true,
            hasError = false
        )

        saveJob = viewModelScope.launch {
            try {
                repository.setFavorite(
                    type = key.first,
                    id = key.second,
                    isFavorite = desiredValue
                )

                if (currentKey == key) {
                    _uiState.update {
                        it.copy(isFavorite = desiredValue)
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                if (currentKey == key) {
                    _uiState.update { it.copy(hasError = true) }
                }
            } finally {
                if (currentKey == key) {
                    _uiState.update { it.copy(isSaving = false) }
                }
            }
        }
    }

    private fun startObserving() {
        val key = currentKey ?: return
        observationJob?.cancel()
        _uiState.value = FavoriteButtonUiState()

        observationJob = viewModelScope.launch {
            try {
                repository.observeIsFavorite(
                    type = key.first,
                    id = key.second
                ).collect { isFavorite ->
                    if (currentKey == key) {
                        _uiState.update {
                            it.copy(isFavorite = isFavorite)
                        }
                    }
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                if (currentKey == key) {
                    _uiState.update { it.copy(hasError = true) }
                }
            }
        }
    }
}