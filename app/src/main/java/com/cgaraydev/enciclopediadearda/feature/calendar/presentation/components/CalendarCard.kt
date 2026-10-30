package com.cgaraydev.enciclopediadearda.feature.calendar.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.tolkienShimmerBrush
import com.cgaraydev.enciclopediadearda.feature.calendar.domain.calendarMonthDays
import com.cgaraydev.enciclopediadearda.ui.theme.RingBearer
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBackground
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden05
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray02
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarCard(
    month: YearMonth,
    selectedDay: Int?,
    onDaySelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shimmerBrush = tolkienShimmerBrush(duration = 2000)
    val weekDays = stringArrayResource(R.array.calendar_week_days)
    val locale = remember { Locale.forLanguageTag("es") }
    val monthNames = remember(locale) {
        Month.entries.map {
            it.getDisplayName(TextStyle.FULL, locale)
        }
    }
    val days = remember(month) { calendarMonthDays(month) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RectangleShape)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = shimmerBrush,
                    style = Stroke(width = 2.dp.toPx())
                )
            }
            .background(TolkienBackground)
            .padding(16.dp)
    ) {
        Text(
            text = monthNames[month.monthValue - 1],
            style = MaterialTheme.typography.titleMedium,
            color = TolkienGray02,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
                .semantics { heading() }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            weekDays.forEach { name ->
                Text(
                    text = name.lowercase(locale),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TolkienGray02,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                repeat(7) { index ->
                    val day = week.getOrNull(index)

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .aspectRatio(1f)
                    ) {
                        if (day != null) {
                            val description = stringResource(
                                R.string.calendar_date,
                                day,
                                monthNames[month.monthValue - 1]
                            )

                            CalendarDay(
                                day = day,
                                selected = day == selectedDay,
                                description = description,
                                onClick = { onDaySelected(day) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }
        }

        HorizontalDivider(
            color = TolkienGray02,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            monthNames.chunked(4).forEachIndexed { rowIndex, names ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    names.forEachIndexed { columnIndex, name ->
                        val monthNumber = rowIndex * 4 + columnIndex + 1

                        CalendarMonth(
                            name = name,
                            selected = month.monthValue == monthNumber,
                            onClick = { onMonthSelected(monthNumber) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDay(
    day: Int,
    selected: Boolean,
    description: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (selected) TolkienGolden05 else TolkienBackground,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "calendarDayBackground"
    )
    val foreground by animateColorAsState(
        targetValue = if (selected) TolkienBackground else TolkienGolden05,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "calendarDayForeground"
    )

    Box(
        modifier = modifier
            .background(background)
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            fontFamily = RingBearer,
            fontSize = 18.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = foreground
        )
    }
}

@Composable
private fun CalendarMonth(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background by animateColorAsState(
        targetValue = if (selected) TolkienGolden05 else TolkienBackground,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "calendarMonthBackground"
    )
    val foreground by animateColorAsState(
        targetValue = if (selected) TolkienBackground else TolkienGolden05,
        animationSpec = tween(300, easing = FastOutSlowInEasing),
        label = "calendarMonthForeground"
    )

    Box(
        modifier = modifier
            .heightIn(min = 48.dp)
            .background(background)
            .selectable(
                selected = selected,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                role = Role.Button,
                onClick = onClick
            )
            .semantics { contentDescription = name }
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name.take(3),
            fontFamily = RingBearer,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            color = foreground
        )
    }
}