package com.cgaraydev.enciclopediadearda.feature.characters.presentation

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
class CharactersViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadCharacters()
    }

    fun retry() {
        if (_uiState.value == CharactersUiState.Error) {
            loadCharacters()
        }
    }

    private fun loadCharacters() {
        if (loadJob?.isActive == true) return

        _uiState.value = CharactersUiState.Loading

        loadJob = viewModelScope.launch {
            try {
                val characters = repository.getCharacters()
                _uiState.value = CharactersUiState.Content(characters)
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = CharactersUiState.Error
            }
        }
    }
}