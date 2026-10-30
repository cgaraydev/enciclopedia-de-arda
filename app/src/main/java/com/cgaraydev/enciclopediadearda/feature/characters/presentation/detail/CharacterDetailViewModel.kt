package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailUiState
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState<Character>>(DetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentId: String? = null
    private var loadJob: Job? = null

    fun loadCharacter(id: String) {
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
                val character = repository.getCharacterById(id)

                _uiState.value = if (character == null) {
                    DetailUiState.NotFound
                } else {
                    DetailUiState.Content(character)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = DetailUiState.Error
            }
        }
    }
}