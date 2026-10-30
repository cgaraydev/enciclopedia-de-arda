package com.cgaraydev.enciclopediadearda.feature.calendar.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components.CalendarCard
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components.TodayInArdaSection
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray02

@Composable
fun CalendarRoute(
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uriHandler = LocalUriHandler.current

    CalendarScreen(
        uiState = uiState,
        onDaySelected = viewModel::selectDay,
        onMonthSelected = viewModel::selectMonth,
        onRetry = viewModel::retry,
        onLinkClick = { url ->
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
        modifier = modifier
    )
}

@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onDaySelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))

        CategoriesHeader(textRes = R.string.calendar_title)

        Spacer(Modifier.height(16.dp))

        CalendarCard(
            month = uiState.month,
            selectedDay = uiState.selectedDay,
            onDaySelected = onDaySelected,
            onMonthSelected = onMonthSelected,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(16.dp))

        val events = uiState.events

        if (events == null) {
            Text(
                text = stringResource(R.string.calendar_select_day),
                style = MaterialTheme.typography.bodyMedium,
                color = TolkienGray02,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(40.dp))
        } else {
            TodayInArdaSection(
                uiState = events,
                onRetry = onRetry,
                onLinkClick = onLinkClick,
                showHeading = false,
                emptyTextRes = R.string.calendar_day_empty
            )
        }
    }
}