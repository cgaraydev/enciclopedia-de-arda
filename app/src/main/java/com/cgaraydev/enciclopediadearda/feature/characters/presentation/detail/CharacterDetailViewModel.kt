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

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CharacterDetailUiState>(
            CharacterDetailUiState.Loading
        )
    val uiState = _uiState.asStateFlow()

    private var currentId: String? = null
    private var loadJob: Job? = null

    fun loadCharacter(id: String) {
        if (currentId == id) return

        currentId = id
        startLoading(id)
    }

    fun retry() {
        if (_uiState.value != CharacterDetailUiState.Error) return

        currentId?.let { startLoading(it) }
    }

    private fun startLoading(id: String) {
        loadJob?.cancel()
        _uiState.value = CharacterDetailUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                val character = repository.getCharacterById(id)

                _uiState.value = if (character == null) {
                    CharacterDetailUiState.NotFound
                } else {
                    CharacterDetailUiState.Content(character)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = CharacterDetailUiState.Error
            }
        }
    }
}