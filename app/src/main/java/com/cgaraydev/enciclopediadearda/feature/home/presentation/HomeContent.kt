package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.IntroText
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.TodayInArdaUiState
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.FeaturedArticleSection
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.HomeDailyEventsSection
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.HomeExploreSection
import com.cgaraydev.enciclopediadearda.feature.home.presentation.components.HomeSectionsSection

@Composable
fun HomeContent(
    uiState: HomeUiState,
    todayState: TodayInArdaUiState,
    onNavigate: (HomeNavigationAction) -> Unit,
    onRandomArticleClick: () -> Unit,
    onRetry: () -> Unit,
    onTodayRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        item(key = "introduction") {
            IntroText(R.string.home_intro)
            Spacer(Modifier.height(40.dp))
        }

        item(key = "explore") {
            HomeExploreSection(
                uiState = uiState,
                onRandomArticleClick = onRandomArticleClick,
                onRetry = onRetry,
                onSearchClick = {
                    onNavigate(HomeNavigationAction.OpenSearch)
                }
            )
        }

        val featuredArticle =
            (uiState as? HomeUiState.Content)?.featuredCharacter

        if (featuredArticle != null) {
            item(key = "featured-article") {
                FeaturedArticleSection(
                    article = featuredArticle,
                    onArticleClick = {
                        onNavigate(HomeNavigationAction.OpenArticle(it))
                    }
                )
            }
        }

        item(key = "today-in-arda") {
            HomeDailyEventsSection(
                uiState = todayState,
                onRetry = onTodayRetry,
                onLinkClick = {
                    onNavigate(HomeNavigationAction.OpenLink(it))
                },
                onCalendarClick = {
                    onNavigate(HomeNavigationAction.OpenCalendar)
                }
            )
        }

        item(key = "main-sections") {
            HomeSectionsSection(
                onSectionClick = {
                    onNavigate(HomeNavigationAction.OpenSection(it))
                }
            )
        }

        item(key = "bottom-spacing") {
            Spacer(Modifier.height(40.dp))
        }
    }
}