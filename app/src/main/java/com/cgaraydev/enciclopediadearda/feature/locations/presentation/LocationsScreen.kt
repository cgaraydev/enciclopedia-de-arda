package com.cgaraydev.enciclopediadearda.feature.locations.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.IntroText
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.core.designsystem.components.RandomExplorerCard
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ScreenHeader
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienBlack
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden07
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun LocationsRoute(
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LocationsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LocationsScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onLocationClick = onLocationClick,
        modifier = modifier
    )
}

@Composable
fun LocationsScreen(
    uiState: LocationsUiState,
    onRetry: () -> Unit,
    onLocationClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ScreenHeader(
            labelRes = R.string.locations_title,
            imageRes = R.drawable.header_locations
        )

        Spacer(Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                LocationsUiState.Loading -> LoadingIndicator()

                LocationsUiState.Error -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.locations_load_error),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TolkienWhite,
                            textAlign = TextAlign.Center
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

                is LocationsUiState.Content -> {
                    if (uiState.locations.isEmpty()) {
                        Text(
                            text = stringResource(R.string.locations_empty),
                            style = MaterialTheme.typography.bodyLarge,
                            color = TolkienWhite,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 40.dp)
                        ) {
                            item(key = "locations-intro") {
                                IntroText(R.string.locations_intro)
                                Spacer(Modifier.height(40.dp))
                            }

                            item(key = "random-location") {
                                RandomExplorerCard(
                                    onClick = {
                                        uiState.locations.randomOrNull()?.let {
                                            onLocationClick(it.id)
                                        }
                                    }
                                )
                                Spacer(Modifier.height(40.dp))
                            }

                            item(key = "other-locations") {
                                CategoriesHeader(
                                    textRes = R.string.locations_other
                                )

                                DemoLocationsCategory(
                                    locations = uiState.locations,
                                    onLocationClick = onLocationClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DemoLocationsCategory(
    locations: List<Location>,
    onLocationClick: (String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RectangleShape,
        colors = CardDefaults.cardColors(
            containerColor = TolkienBlack,
            contentColor = TolkienWhite
        ),
        border = BorderStroke(1.dp, TolkienGold.copy(alpha = 0.7f))
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(role = Role.Button) {
                        expanded = !expanded
                    }
                    .heightIn(min = 48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.locations_other).lowercase(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = stringResource(
                        R.string.category_item_count,
                        locations.size
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = TolkienWhite.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    painter = painterResource(R.drawable.ic_right),
                    contentDescription = stringResource(
                        if (expanded) {
                            R.string.collapse_section
                        } else {
                            R.string.expand_section
                        }
                    ),
                    tint = TolkienWhite,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer {
                            rotationZ = if (expanded) -90f else 90f
                        }
                )
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    locations.forEachIndexed { index, location ->
                        key(location.id) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(role = Role.Button) {
                                        onLocationClick(location.id)
                                    }
                                    .heightIn(min = 48.dp)
                                    .padding(
                                        horizontal = 24.dp,
                                        vertical = 8.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = location.name.lowercase(),
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TolkienWhite,
                                    modifier = Modifier.weight(1f)
                                )

                                Icon(
                                    painter = painterResource(R.drawable.ic_right),
                                    contentDescription = null,
                                    tint = TolkienWhite,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            if (index != locations.lastIndex) {
                                HorizontalDivider(
                                    color = TolkienGolden07,
                                    modifier = Modifier.padding(
                                        horizontal = 16.dp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}