package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ScreenHeader
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaUiState
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    todayState: TodayInArdaUiState,
    onNavigate: (HomeNavigationAction) -> Unit,
    onRandomArticleClick: () -> Unit,
    onRetry: () -> Unit,
    onTodayRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ScreenHeader(
            labelRes = R.string.home_header,
            color = TolkienGold,
            height = 100.dp
        )

        Spacer(Modifier.height(40.dp))

        HomeContent(
            uiState = uiState,
            todayState = todayState,
            onNavigate = onNavigate,
            onRandomArticleClick = onRandomArticleClick,
            onRetry = onRetry,
            onTodayRetry = onTodayRetry,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
    }
}