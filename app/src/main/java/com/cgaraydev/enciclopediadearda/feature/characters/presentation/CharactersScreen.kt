package com.cgaraydev.enciclopediadearda.feature.characters.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ScreenHeader
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import com.cgaraydev.enciclopediadearda.core.designsystem.components.RandomExplorerCard
import com.cgaraydev.enciclopediadearda.core.designsystem.components.IntroText
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.res.painterResource
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun CharactersRoute(
    onCharacterClick: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CharactersScreen(
        uiState = uiState,
        onRetry = viewModel::retry,
        onCharacterClick = onCharacterClick,
        onFavoritesClick = onFavoritesClick,
        modifier = modifier
    )
}

@Composable
fun CharactersScreen(
    uiState: CharactersUiState,
    onRetry: () -> Unit,
    onCharacterClick: (String) -> Unit,
    onFavoritesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
//        ScreenHeader(
//            labelRes = R.string.characters_title,
//            imageRes = R.drawable.header_characters
//        )
        Box(modifier = Modifier.fillMaxWidth()) {
            ScreenHeader(
                labelRes = R.string.characters_title,
                imageRes = R.drawable.header_characters
            )

            IconButton(
                onClick = onFavoritesClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_favorite_filled),
                    contentDescription = stringResource(R.string.favorite_header),
                    tint = TolkienWhite
                )
            }
        }
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                CharactersUiState.Loading -> {
                    CircularProgressIndicator()
                }

                CharactersUiState.Error -> {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.characters_load_error),
                            textAlign = TextAlign.Center
                        )
                        Button(onClick = onRetry) {
                            Text(stringResource(R.string.retry))
                        }
                    }
                }

                is CharactersUiState.Content -> {
                    if (uiState.characters.isEmpty()) {
                        Text(
                            text = stringResource(R.string.characters_empty),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            item(key = "characters-introduction") {
                                IntroText(R.string.characters_intro)
                            }

                            item(key = "characters-introduction-spacing") {
                                Spacer(modifier = Modifier.height(40.dp))
                            }
                            item(key = "random-character") {
                                RandomExplorerCard(
                                    onClick = {
                                        uiState.characters.randomOrNull()?.let { character ->
                                            onCharacterClick(character.id)
                                        }
                                    }
                                )
                            }

                            item(key = "random-character-spacing") {
                                Spacer(Modifier.height(40.dp))
                            }
                            items(
                                items = uiState.characters,
                                key = { it.id }
                            ) { character ->
                                Card(
                                    onClick = { onCharacterClick(character.id) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    Text(
                                        text = character.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}