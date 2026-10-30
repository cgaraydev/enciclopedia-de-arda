package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.IntroText
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.core.designsystem.components.RandomExplorerCard
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ScreenHeader
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.HomeSectionCard
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.core.designsystem.components.SearchExplorerCard

@Composable
fun HomeRoute(
    onCharactersClick: () -> Unit,
    onCharacterClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        uiState = uiState,
        onCharactersClick = onCharactersClick,
        onRandomArticleClick = {
            val content = uiState as? HomeUiState.Content
            content?.characterIds?.randomOrNull()?.let { id ->
                onCharacterClick(id)
            }
        },
        onRetry = viewModel::retry,
        onSearchClick = onSearchClick,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onCharactersClick: () -> Unit,
    onRandomArticleClick: () -> Unit,
    onRetry: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ScreenHeader(
            labelRes = R.string.home_header,
            color = TolkienGold,
            height = 100.dp
        )

        Spacer(Modifier.height(40.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            item(key = "introduction") {
                IntroText(R.string.home_intro)
                Spacer(Modifier.height(40.dp))
            }

            item(key = "explore") {
                CategoriesHeader(textRes = R.string.explore)

                HomeExplorerSection(
                    uiState = uiState,
                    onRandomArticleClick = onRandomArticleClick,
                    onRetry = onRetry
                )

                Spacer(Modifier.height(20.dp))

                SearchExplorerCard(
                    onClick = onSearchClick
                )

                Spacer(Modifier.height(40.dp))
            }

            item(key = "sections-heading") {
                CategoriesHeader(textRes = R.string.main_sections)
            }

            item(key = "characters-section") {
                HomeSectionCard(
                    titleRes = R.string.characters_title,
                    imageRes = R.drawable.header_characters,
                    onClick = onCharactersClick
                )
            }

            item(key = "bottom-spacing") {
                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
private fun HomeExplorerSection(
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
            if (uiState.characterIds.isEmpty()) {
                Text(
                    text = stringResource(R.string.home_catalog_empty),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            } else {
                RandomExplorerCard(
                    onClick = onRandomArticleClick
                )
            }
        }
    }
}