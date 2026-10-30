package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ArticleDetailHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CharacteristicsSection
import com.cgaraydev.enciclopediadearda.core.designsystem.components.DetailImage
import com.cgaraydev.enciclopediadearda.core.designsystem.components.HtmlText
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ImageCarousel
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.core.designsystem.components.WikiLinksSection
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.favorites.presentation.FavoriteButtonRoute

@Composable
fun LocationDetailRoute(
    locationId: String,
    onLocationClick: (String) -> Unit,
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(locationId) {
        viewModel.loadLocation(locationId)
    }

    LocationDetailScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onLinkClick = { url ->
            when {
                url.startsWith("location:") -> {
                    val id = url.removePrefix("location:")
                    if (id.isNotBlank()) onLocationClick(id)
                }

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
fun LocationDetailScreen(
    uiState: LocationDetailUiState,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        LocationDetailUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        LocationDetailUiState.Error,
        LocationDetailUiState.NotFound -> {
            Column(
                modifier = modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterVertically
                )
            ) {
                Text(
                    text = stringResource(
                        if (uiState == LocationDetailUiState.NotFound) {
                            R.string.item_not_found
                        } else {
                            R.string.location_load_error
                        }
                    ),
                    style = MaterialTheme.typography.bodyLarge,
                    color = TolkienWhite,
                    textAlign = TextAlign.Center
                )

                if (uiState == LocationDetailUiState.Error) {
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

        is LocationDetailUiState.Content -> {
            val location = uiState.location
            val scrollState = rememberScrollState()

            Column(modifier = modifier) {
                ArticleDetailHeader(
                    title = location.name,
                    scrollState = scrollState,
                    categoryHtml = location.typeHtml,
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
                        imagePath = location.imagePath,
                        detailName = location.name
                    )
                    FavoriteButtonRoute(
                        articleType = ArticleType.LOCATION,
                        articleId = location.id
                    )

                    Spacer(Modifier.height(20.dp))

                    CharacteristicsSection(
                        labelValuePairs = listOf(
                            stringResource(R.string.location_other_names) to
                                    location.otherNames,
                            stringResource(R.string.location_capital) to
                                    location.capital,
                            stringResource(R.string.location_founded) to
                                    location.founded,
                            stringResource(R.string.location_destroyed) to
                                    location.destroyed,
                            stringResource(R.string.location_position) to
                                    location.location,
                            stringResource(R.string.location_major_towns) to
                                    location.majorTowns,
                            stringResource(R.string.location_regions) to
                                    location.regions,
                            stringResource(R.string.location_events) to
                                    location.events,
                            stringResource(R.string.location_languages) to
                                    location.languages,
                            stringResource(R.string.location_inhabitants) to
                                    location.inhabitants,
                            stringResource(R.string.location_governance) to
                                    location.governance
                        ),
                        onLinkClick = onLinkClick
                    )

                    LocationTextSection(
                        titleRes = R.string.history,
                        html = location.historyHtml,
                        onLinkClick = onLinkClick
                    )

                    LocationTextSection(
                        titleRes = R.string.etymology,
                        html = location.etymologyHtml,
                        onLinkClick = onLinkClick
                    )

                    ImageCarousel(
                        images = location.images,
                        onLinkClick = onLinkClick
                    )

                    WikiLinksSection(
                        links = location.externalLinks,
                        onLinkClick = onLinkClick
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
private fun LocationTextSection(
    @StringRes titleRes: Int,
    html: String?,
    onLinkClick: (String) -> Unit
) {
    if (html.isNullOrBlank()) return

    CategoriesHeader(textRes = titleRes)

    HtmlText(
        html = html,
        onLinkClick = onLinkClick,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(Modifier.height(20.dp))
}