package com.cgaraydev.enciclopediadearda.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaUiState
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components.TodayInArdaSection
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold

@Composable
fun HomeDailyEventsSection(
    uiState: TodayInArdaUiState,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    onCalendarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        TodayInArdaSection(
            uiState = uiState,
            onRetry = onRetry,
            onLinkClick = onLinkClick
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