package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.FavoriteToggleButton
import com.cgaraydev.enciclopediadearda.core.model.ArticleType

@Composable
fun FavoriteButtonRoute(
    articleType: ArticleType,
    articleId: String,
    modifier: Modifier = Modifier,
    viewModel: FavoriteButtonViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(articleType, articleId) {
        viewModel.load(articleType, articleId)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FavoriteToggleButton(
            isFavorite = state.isFavorite == true,
            enabled = state.isFavorite != null && !state.isSaving,
            onFavoriteClick = viewModel::toggleFavorite
        )

        if (state.hasError) {
            Text(
                text = stringResource(R.string.favorites_access_error),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            TextButton(
                onClick = viewModel::retry,
                enabled = !state.isSaving
            ) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}