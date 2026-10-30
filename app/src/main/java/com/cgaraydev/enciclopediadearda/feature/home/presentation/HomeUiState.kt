package com.cgaraydev.enciclopediadearda.feature.home.presentation

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(
        val characterIds: List<String>,
        val featuredCharacter: Character? = null
    ) : HomeUiState

    data object Error : HomeUiState
}