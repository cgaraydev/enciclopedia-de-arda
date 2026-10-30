package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun retry() {
        if (_uiState.value == HomeUiState.Error) {
            loadCatalog()
        }
    }

    private fun loadCatalog() {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            try {
                val characters = characterRepository.getCharacters()

                _uiState.value = HomeUiState.Content(
                    characterIds = characters.map { it.id },
                    featuredCharacter = characters.randomOrNull()
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = HomeUiState.Error
            }
        }
    }
}