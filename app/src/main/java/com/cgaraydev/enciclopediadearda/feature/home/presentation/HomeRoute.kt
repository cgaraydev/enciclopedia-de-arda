package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun HomeRoute(
    onNavigate: (HomeNavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    todayViewModel: TodayInArdaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val todayState by todayViewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner, todayViewModel) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            while (isActive) {
                todayViewModel.refresh()
                delay(60_000L)
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onCharactersClick = {
            onNavigate(
                HomeNavigationAction.OpenSection(ArticleType.CHARACTER)
            )
        },
        onRandomArticleClick = {
            val content = uiState as? HomeUiState.Content

            content?.articleReferences?.randomOrNull()?.let {
                onNavigate(HomeNavigationAction.OpenArticle(it))
            }
        },
        onRetry = viewModel::retry,
        onSearchClick = {
            onNavigate(HomeNavigationAction.OpenSearch)
        },
        onFeaturedClick = { id ->
            onNavigate(
                HomeNavigationAction.OpenArticle(
                    ArticleReference(
                        type = ArticleType.CHARACTER,
                        id = id
                    )
                )
            )
        },
        todayState = todayState,
        onTodayRetry = todayViewModel::retry,
        onCalendarLinkClick = { url ->
            onNavigate(HomeNavigationAction.OpenLink(url))
        },
        onCalendarClick = {
            onNavigate(HomeNavigationAction.OpenCalendar)
        },
        modifier = modifier
    )
}