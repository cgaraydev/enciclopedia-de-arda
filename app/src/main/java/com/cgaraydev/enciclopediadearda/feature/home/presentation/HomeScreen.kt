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
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.FeaturedArticleSection
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalUriHandler
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaUiState
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaViewModel
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components.TodayInArdaSection
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.time.Duration.Companion.milliseconds
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import com.cgaraydev.enciclopediadearda.core.model.ArticleType

@Composable
fun HomeRoute(
    onCharactersClick: () -> Unit,
    onCharacterClick: (String) -> Unit,
    onLocationClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    todayViewModel: TodayInArdaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val todayState by todayViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(lifecycleOwner, todayViewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                todayViewModel.refresh()
                delay(60_000.milliseconds)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onCharactersClick = onCharactersClick,
        onRandomArticleClick = {
            val content = uiState as? HomeUiState.Content

            content?.articleReferences?.randomOrNull()?.let { article ->
                when (article.type) {
                    ArticleType.CHARACTER -> onCharacterClick(article.id)
                    ArticleType.LOCATION -> onLocationClick(article.id)
                }
            }
        },
        onRetry = viewModel::retry,
        onSearchClick = onSearchClick,
        onFeaturedClick = onCharacterClick,
        todayState = todayState,
        onTodayRetry = todayViewModel::retry,
        onCalendarLinkClick = { url ->
            when {
                url.startsWith("character:") -> {
                    val id = url.removePrefix("character:")
                    if (id.isNotBlank()) {
                        onCharacterClick(id)
                    }
                }

                url.startsWith("https://") || url.startsWith("http://") -> {
                    uriHandler.openUri(url)
                }
            }
        },
        onCalendarClick = onCalendarClick,
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
    onFeaturedClick: (String) -> Unit,
    todayState: TodayInArdaUiState,
    onTodayRetry: () -> Unit,
    onCalendarLinkClick: (String) -> Unit,
    onCalendarClick: () -> Unit,
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

            val featuredCharacter =
                (uiState as? HomeUiState.Content)?.featuredCharacter

            if (featuredCharacter != null) {
                item(key = "featured-article") {
                    FeaturedArticleSection(
                        character = featuredCharacter,
                        onCharacterClick = onFeaturedClick
                    )
                }
            }

            item(key = "today-in-arda") {
                Column {
                    TodayInArdaSection(
                        uiState = todayState,
                        onRetry = onTodayRetry,
                        onLinkClick = onCalendarLinkClick
                    )

                    TextButton(
                        onClick = onCalendarClick,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.calendar_open),
                            style = MaterialTheme.typography.titleSmall,
                            color = TolkienGold
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
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
            if (uiState.articleReferences.isEmpty()) {
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