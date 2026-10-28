package com.cgaraydev.enciclopediadearda.feature.characters.presentation

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface CharactersUiState {
    data object Loading : CharactersUiState

    data class Content(
        val characters: List<Character>
    ) : CharactersUiState

    data object Error : CharactersUiState
}