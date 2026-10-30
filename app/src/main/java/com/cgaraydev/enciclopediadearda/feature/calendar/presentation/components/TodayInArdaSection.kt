package com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.HtmlText
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.CalendarEvent
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaUiState
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray02
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienRed
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun TodayInArdaSection(
    uiState: TodayInArdaUiState,
    onRetry: () -> Unit,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showHeading: Boolean = true,
    emptyTextRes: Int = R.string.calendar_today_empty
) {
    val monthName = remember(uiState.date.month) {
        uiState.date.month
            .getDisplayName(
                TextStyle.FULL,
                Locale.forLanguageTag("es")
            )
            .replaceFirstChar { it.uppercaseChar() }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        if (showHeading) {
            CategoriesHeader(textRes = R.string.today_in_arda)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(
                    R.string.calendar_date,
                    uiState.date.dayOfMonth,
                    monthName
                ),
                style = MaterialTheme.typography.titleMedium,
                color = TolkienWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .semantics { heading() }
            )

            Spacer(Modifier.height(12.dp))

            AnimatedContent(
                targetState = uiState,
                contentKey = { it::class },
                transitionSpec = {
                    val enter = slideInVertically(
                        initialOffsetY = { it / 4 },
                        animationSpec = tween(
                            400,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeIn(tween(400))

                    val exit = slideOutVertically(
                        targetOffsetY = { -it / 4 },
                        animationSpec = tween(
                            400,
                            easing = FastOutSlowInEasing
                        )
                    ) + fadeOut(tween(400))

                    enter togetherWith exit
                },
                label = "todayEvents"
            ) { state ->
                when (state) {
                    is TodayInArdaUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(72.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }

                    is TodayInArdaUiState.Error -> {
                        Column {
                            Text(
                                text = stringResource(
                                    R.string.calendar_load_error
                                ),
                                style = MaterialTheme.typography.bodySmall,
                                color = TolkienRed.copy(alpha = 0.9f)
                            )

                            TextButton(onClick = onRetry) {
                                Text(
                                    text = stringResource(R.string.retry),
                                    style = MaterialTheme.typography.titleSmall,
                                    color = TolkienGold
                                )
                            }
                        }
                    }

                    is TodayInArdaUiState.Content -> {
                        if (state.events.isEmpty()) {
                            Text(
                                text = stringResource(
                                    emptyTextRes
                                ),
                                style = MaterialTheme.typography.bodyMedium,
                                color = TolkienGray02
                            )
                        } else {
                            DailyEventsContent(
                                events = state.events,
                                onLinkClick = onLinkClick
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun DailyEventsContent(
    events: List<CalendarEvent>,
    onLinkClick: (String) -> Unit
) {
    val groupedEvents = remember(events) {
        events.groupBy { it.year }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        groupedEvents.forEach { (year, yearEvents) ->
            if (year != null) {
                Text(
                    text = stringResource(
                        R.string.calendar_year,
                        year.uppercase(Locale.ROOT)
                    ),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TolkienGold,
                    modifier = Modifier
                        .padding(bottom = 4.dp)
                        .semantics { heading() }
                )
            }

            yearEvents.forEach { event ->
                Row(
                    modifier = Modifier.padding(
                        start = if (year != null) 12.dp else 0.dp
                    ),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_ring),
                        contentDescription = null,
                        tint = TolkienGray02,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(14.dp)
                    )

                    Spacer(Modifier.width(6.dp))

                    HtmlText(
                        html = event.descriptionHtml,
                        onLinkClick = onLinkClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}