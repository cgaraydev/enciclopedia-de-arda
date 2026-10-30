package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CharacteristicsSection
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location

@Composable
fun LocationCharacteristicsSection(
    location: Location,
    onLinkClick: (String) -> Unit
) {
    CharacteristicsSection(
        labelValuePairs = listOf(
            stringResource(R.string.location_other_names) to
                    location.otherNames,
            stringResource(R.string.location_capital) to
                    location.capital,
            stringResource(R.string.location_founded) to
                    location.founded,
            stringResource(R.string.location_destroyed) to
                    location.destroyed,
            stringResource(R.string.location_position) to
                    location.location,
            stringResource(R.string.location_major_towns) to
                    location.majorTowns,
            stringResource(R.string.location_regions) to
                    location.regions,
            stringResource(R.string.location_events) to
                    location.events,
            stringResource(R.string.location_languages) to
                    location.languages,
            stringResource(R.string.location_inhabitants) to
                    location.inhabitants,
            stringResource(R.string.location_governance) to
                    location.governance
        ),
        onLinkClick = onLinkClick
    )
}