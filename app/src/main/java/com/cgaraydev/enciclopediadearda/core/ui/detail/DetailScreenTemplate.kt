package com.cgaraydev.enciclopediadearda.core.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun <T> DetailScreenTemplate(
    uiState: DetailUiState<T>,
    @StringRes errorMessageRes: Int,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (T) -> Unit
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            DetailUiState.Loading -> LoadingIndicator()

            DetailUiState.Error -> {
                DetailMessage(
                    messageRes = errorMessageRes,
                    onRetry = onRetry
                )
            }

            DetailUiState.NotFound -> {
                DetailMessage(
                    messageRes = R.string.item_not_found,
                    onRetry = null
                )
            }

            is DetailUiState.Content -> content(uiState.item)
        }
    }
}

@Composable
private fun DetailMessage(
    @StringRes messageRes: Int,
    onRetry: (() -> Unit)?
) {
    Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(messageRes),
            style = MaterialTheme.typography.bodyLarge,
            color = TolkienWhite,
            textAlign = TextAlign.Center
        )

        if (onRetry != null) {
            TextButton(onClick = onRetry) {
                Text(
                    text = stringResource(R.string.retry),
                    style = MaterialTheme.typography.titleSmall,
                    color = TolkienGold
                )
            }
        }
    }
}