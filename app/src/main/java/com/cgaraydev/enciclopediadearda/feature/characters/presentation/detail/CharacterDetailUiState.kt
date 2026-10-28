package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface CharacterDetailUiState {
    data object Loading : CharacterDetailUiState

    data class Content(
        val character: Character
    ) : CharacterDetailUiState

    data object NotFound : CharacterDetailUiState

    data object Error : CharacterDetailUiState
}