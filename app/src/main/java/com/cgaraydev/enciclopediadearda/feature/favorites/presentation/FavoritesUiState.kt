package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data class Content(
        val characters: List<Character>
    ) : FavoritesUiState

    data object Error : FavoritesUiState
}