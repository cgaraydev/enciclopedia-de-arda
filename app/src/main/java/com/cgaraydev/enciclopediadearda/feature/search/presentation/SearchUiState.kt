package com.cgaraydev.enciclopediadearda.feature.search.presentation

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface SearchUiState {
    data object Idle : SearchUiState
    data object Searching : SearchUiState
    data object Error : SearchUiState

    data class Results(
        val characters: List<Character>
    ) : SearchUiState
}