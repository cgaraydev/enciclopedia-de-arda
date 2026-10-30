package com.cgaraydev.enciclopediadearda.feature.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.core.designsystem.components.RandomExplorerCard
import com.cgaraydev.enciclopediadearda.core.designsystem.components.SearchExplorerCard
import com.cgaraydev.enciclopediadearda.feature.home.presentation.HomeUiState

@Composable
fun HomeExploreSection(
    uiState: HomeUiState,
    onRandomArticleClick: () -> Unit,
    onRetry: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CategoriesHeader(textRes = R.string.explore)

        HomeRandomArticleSection(
            uiState = uiState,
            onRandomArticleClick = onRandomArticleClick,
            onRetry = onRetry
        )

        Spacer(Modifier.height(20.dp))

        SearchExplorerCard(onClick = onSearchClick)

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun HomeRandomArticleSection(
    uiState: HomeUiState,
    onRandomArticleClick: () -> Unit,
    onRetry: () -> Unit
) {
    when (uiState) {
        HomeUiState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        HomeUiState.Error -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        R.string.home_catalog_load_error
                    ),
                    textAlign = TextAlign.Center
                )

                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }

        is HomeUiState.Content -> {
            if (uiState.articleReferences.isEmpty()) {
                Text(
                    text = stringResource(R.string.home_catalog_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            } else {
                RandomExplorerCard(onClick = onRandomArticleClick)
            }
        }
    }
}