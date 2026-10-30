package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CharacteristicsSection
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character

@Composable
fun CharacterCharacteristicsSection(
    character: Character,
    onLinkClick: (String) -> Unit
) {
    CharacteristicsSection(
        labelValuePairs = listOf(
            stringResource(R.string.character_other_names) to
                    character.otherNames,
            stringResource(R.string.character_birth) to
                    character.birth,
            stringResource(R.string.character_death) to
                    character.death,
            stringResource(R.string.character_titles) to
                    character.titles,
            stringResource(R.string.character_partner) to
                    character.love,
            stringResource(R.string.character_family) to
                    character.family,
            stringResource(R.string.character_house) to
                    character.house
        ),
        onLinkClick = onLinkClick
    )
}