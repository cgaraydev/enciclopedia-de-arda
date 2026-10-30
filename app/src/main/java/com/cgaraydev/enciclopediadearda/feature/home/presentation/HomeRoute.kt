package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaLifecycleEffect
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaViewModel

@Composable
fun HomeRoute(
    onNavigate: (HomeNavigationAction) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    todayViewModel: TodayInArdaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val todayState by todayViewModel.uiState.collectAsStateWithLifecycle()

    TodayInArdaLifecycleEffect(
        onRefresh = { todayViewModel.refresh() }
    )

    HomeScreen(
        uiState = uiState,
        todayState = todayState,
        onNavigate = onNavigate,
        onRandomArticleClick = {
            val content = uiState as? HomeUiState.Content

            content?.articleReferences?.randomOrNull()?.let {
                onNavigate(HomeNavigationAction.OpenArticle(it))
            }
        },
        onRetry = viewModel::retry,
        onTodayRetry = todayViewModel::retry,
        modifier = modifier
    )
}