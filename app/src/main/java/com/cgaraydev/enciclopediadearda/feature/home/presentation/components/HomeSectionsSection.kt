package com.cgaraydev.enciclopediadearda.feature.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.model.ArticleType

@Composable
fun HomeSectionsSection(
    onSectionClick: (ArticleType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        CategoriesHeader(textRes = R.string.main_sections)

        HomeSectionCard(
            titleRes = R.string.characters_title,
            imageRes = R.drawable.header_characters,
            onClick = {
                onSectionClick(ArticleType.CHARACTER)
            }
        )
    }
}