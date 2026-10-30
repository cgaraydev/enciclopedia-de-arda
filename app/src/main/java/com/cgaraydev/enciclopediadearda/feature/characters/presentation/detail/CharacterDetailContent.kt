package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ImageCarousel
import com.cgaraydev.enciclopediadearda.core.designsystem.components.WikiLinksSection
import com.cgaraydev.enciclopediadearda.core.ui.detail.ArticleDetailLayout
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailTextSection
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail.components.CharacterCharacteristicsSection

@Composable
fun CharacterDetailContent(
    character: Character,
    onLinkClick: (String) -> Unit,
    favoriteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    ArticleDetailLayout(
        item = character,
        onLinkClick = onLinkClick,
        favoriteAction = favoriteAction,
        modifier = modifier
    ) {
        CharacterCharacteristicsSection(
            character = character,
            onLinkClick = onLinkClick
        )

        DetailTextSection(
            titleRes = R.string.biography,
            html = character.biographyHtml,
            onLinkClick = onLinkClick
        )

        DetailTextSection(
            titleRes = R.string.etymology,
            html = character.etymologyHtml,
            onLinkClick = onLinkClick
        )

        ImageCarousel(
            images = character.images,
            onLinkClick = onLinkClick
        )

        WikiLinksSection(
            links = character.externalLinks,
            onLinkClick = onLinkClick
        )
    }
}