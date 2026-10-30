package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CharacteristicsSection
import com.cgaraydev.enciclopediadearda.core.designsystem.components.DetailImage
import com.cgaraydev.enciclopediadearda.core.designsystem.components.HtmlText
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ImageCarousel
import com.cgaraydev.enciclopediadearda.core.designsystem.components.WikiLinksSection
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.favorites.presentation.FavoriteButtonRoute
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ArticleDetailHeader


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
                ArticleDetailHeader(
                    title = uiState.character.name,
                    scrollState = scrollState,
                    onLinkClick = onLinkClick
                )
                Spacer(Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                ) {
                    DetailImage(
                        imagePath = uiState.character.imagePath,
                        detailName = uiState.character.name
                    )
                    FavoriteButtonRoute(
                        articleType = ArticleType.CHARACTER,
                        articleId = uiState.character.id
                    )
                    Spacer(Modifier.height(20.dp))

                    CharacteristicsSection(
                        labelValuePairs = listOf(
                            stringResource(R.string.character_other_names) to
                                    uiState.character.otherNames,
                            stringResource(R.string.character_birth) to
                                    uiState.character.birth,
                            stringResource(R.string.character_death) to
                                    uiState.character.death,
                            stringResource(R.string.character_titles) to
                                    uiState.character.titles,
                            stringResource(R.string.character_partner) to
                                    uiState.character.love,
                            stringResource(R.string.character_family) to
                                    uiState.character.family,
                            stringResource(R.string.character_house) to
                                    uiState.character.house
                        ),
                        onLinkClick = onLinkClick
                    )

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

                    uiState.character.etymologyHtml
                        ?.takeIf { it.isNotBlank() }
                        ?.let { etymology ->
                            CategoriesHeader(
                                textRes = R.string.etymology
                            )
                            HtmlText(
                                html = etymology,
                                onLinkClick = onLinkClick,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.height(20.dp))
                        }

                    ImageCarousel(
                        images = uiState.character.images,
                        onLinkClick = onLinkClick
                    )

                    WikiLinksSection(
                        links = uiState.character.externalLinks,
                        onLinkClick = onLinkClick
                    )
                }
            }
        }
    }
}