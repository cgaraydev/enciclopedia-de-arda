package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailScreenTemplate
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailUiState
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location

@Composable
fun LocationDetailScreen(
    uiState: DetailUiState<Location>,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    favoriteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    DetailScreenTemplate(
        uiState = uiState,
        errorMessageRes = R.string.location_load_error,
        onRetry = onRetry,
        modifier = modifier
    ) { location ->
        LocationDetailContent(
            location = location,
            onLinkClick = onLinkClick,
            favoriteAction = favoriteAction
        )
    }
}