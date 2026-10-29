package com.cgaraydev.enciclopediadearda.feature.favorites.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ScreenHeader
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden07
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun FavoritesRoute(
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onCharacterClick = onCharacterClick,
        modifier = modifier
    )
}

@Composable
fun FavoritesScreen(
    uiState: FavoritesUiState,
    onRetry: () -> Unit,
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        FavoritesUiState.Loading -> {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        FavoritesUiState.Error -> {
            Column(
                modifier = modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(
                    16.dp,
                    Alignment.CenterVertically
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.favorites_load_error),
                    textAlign = TextAlign.Center
                )
                Button(onClick = onRetry) {
                    Text(stringResource(R.string.retry))
                }
            }
        }

        is FavoritesUiState.Content -> {
            if (uiState.characters.isEmpty()) {
                EmptyFavoritesSection(modifier = modifier)
            } else {
                Column(modifier = modifier) {
                    ScreenHeader(labelRes = R.string.favorite_header)

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(
                            horizontal = 8.dp,
                            vertical = 8.dp
                        )
                    ) {
                        item(key = "characters-heading") {
                            Text(
                                text = stringResource(
                                    R.string.characters_title
                                ).lowercase(),
                                style = MaterialTheme.typography.titleMedium,
                                color = TolkienGold,
                                modifier = Modifier.semantics { heading() }
                            )
                            Spacer(Modifier.height(8.dp))
                        }

                        items(
                            items = uiState.characters,
                            key = { "character:${it.id}" }
                        ) { character ->
                            FavoriteRow(
                                name = character.name,
                                onClick = {
                                    onCharacterClick(character.id)
                                }
                            )
                        }

                        item(key = "characters-divider") {
                            Text(
                                text = stringResource(R.string.divider_runes),
                                fontFamily = FontFamily(Font(R.font.dwarf)),
                                fontSize = 4.sp,
                                color = TolkienGolden07,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = 10.dp,
                                        vertical = 8.dp
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FavoriteRow(
    name: String,
    onClick: () -> Unit
) {
    val description = stringResource(R.string.view_details, name)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp)
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
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
}

@Composable
private fun EmptyFavoritesSection(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        ScreenHeader(labelRes = R.string.favorite_header)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ring),
                contentDescription = null,
                tint = TolkienGolden07,
                modifier = Modifier.size(64.dp)
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.no_favorites),
                style = MaterialTheme.typography.titleLarge,
                color = TolkienGolden07,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.add_favorites_here),
                style = MaterialTheme.typography.bodyLarge,
                color = TolkienWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}