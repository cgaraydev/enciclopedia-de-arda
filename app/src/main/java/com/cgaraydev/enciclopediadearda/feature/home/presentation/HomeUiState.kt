package com.cgaraydev.enciclopediadearda.feature.home.presentation

import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(
        val articleReferences: List<ArticleReference>,
        val featuredCharacter: Character? = null
    ) : HomeUiState

    data object Error : HomeUiState
}