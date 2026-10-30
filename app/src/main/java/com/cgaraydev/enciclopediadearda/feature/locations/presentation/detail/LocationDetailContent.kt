package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ImageCarousel
import com.cgaraydev.enciclopediadearda.core.designsystem.components.WikiLinksSection
import com.cgaraydev.enciclopediadearda.core.ui.detail.ArticleDetailLayout
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailTextSection
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail.components.LocationCharacteristicsSection

@Composable
fun LocationDetailContent(
    location: Location,
    onLinkClick: (String) -> Unit,
    favoriteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    ArticleDetailLayout(
        item = location,
        categoryHtml = location.typeHtml,
        onLinkClick = onLinkClick,
        favoriteAction = favoriteAction,
        modifier = modifier
    ) {
        LocationCharacteristicsSection(
            location = location,
            onLinkClick = onLinkClick
        )

        DetailTextSection(
            titleRes = R.string.history,
            html = location.historyHtml,
            onLinkClick = onLinkClick
        )

        DetailTextSection(
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