package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.favorites.presentation.FavoriteButtonRoute

@Composable
fun CharacterDetailRoute(
    characterId: String,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    CharacterDetailScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onLinkClick = onLinkClick,
        favoriteAction = {
            FavoriteButtonRoute(
                articleType = ArticleType.CHARACTER,
                articleId = characterId
            )
        },
        modifier = modifier
    )
}