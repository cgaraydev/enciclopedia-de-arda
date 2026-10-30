package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailScreenTemplate
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailUiState
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

@Composable
fun CharacterDetailScreen(
    uiState: DetailUiState<Character>,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    favoriteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    DetailScreenTemplate(
        uiState = uiState,
        errorMessageRes = R.string.character_load_error,
        onRetry = onRetry,
        modifier = modifier
    ) { character ->
        CharacterDetailContent(
            character = character,
            onLinkClick = onLinkClick,
            favoriteAction = favoriteAction
        )
    }
}