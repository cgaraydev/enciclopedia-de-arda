package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import com.cgaraydev.enciclopediadearda.feature.favorites.domain.FavoriteArticle

sealed interface FavoritesUiState {
    data object Loading : FavoritesUiState

    data class Content(
        val articles: List<FavoriteArticle>
    ) : FavoritesUiState

    data object Error : FavoritesUiState
}