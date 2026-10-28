package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.HtmlText
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader

@Composable
fun CharacterDetailRoute(
    characterId: String,
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(characterId) {
        viewModel.loadCharacter(characterId)
    }

    CharacterDetailScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onLinkClick = { url ->
            when {
                url.startsWith("character:") -> {
                    val id = url.removePrefix("character:")
                    if (id.isNotBlank()) onCharacterClick(id)
                }

                url.startsWith("https://") ||
                        url.startsWith("http://") -> {
                    uriHandler.openUri(url)
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun CharacterDetailScreen(
    uiState: CharacterDetailUiState,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        CharacterDetailUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        CharacterDetailUiState.Error,
        CharacterDetailUiState.NotFound -> {
            Column(
                modifier = modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(
                        if (uiState == CharacterDetailUiState.NotFound) {
                            R.string.item_not_found
                        } else {
                            R.string.character_load_error
                        }
                    ),
                    textAlign = TextAlign.Center
                )

                if (uiState == CharacterDetailUiState.Error) {
                    Button(onClick = onRetry) {
                        Text(stringResource(R.string.retry))
                    }
                }
            }
        }

        is CharacterDetailUiState.Content -> {
            val scrollState = rememberScrollState()

            Column(modifier = modifier) {
                CharacterDetailHeader(
                    title = uiState.character.name,
                    scrollState = scrollState
                )

                Spacer(Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    uiState.character.biographyHtml
                        ?.takeIf { it.isNotBlank() }
                        ?.let { biography ->
                            CategoriesHeader(
                                textRes = R.string.biography
                            )
                            HtmlText(
                                html = biography,
                                onLinkClick = onLinkClick,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.height(20.dp))
                        }
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailHeader(
    title: String,
    scrollState: ScrollState
) {
    val isCollapsed by remember(scrollState) {
        derivedStateOf { scrollState.value > 50 }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.lowercase(),
            style = if (isCollapsed) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.displaySmall
            },
            color = TolkienWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
        )
    }
}