package com.cgaraydev.enciclopediadearda.feature.search.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.designsystem.components.LoadingIndicator
import com.cgaraydev.enciclopediadearda.feature.search.presentation.components.SearchResultsList
import com.cgaraydev.enciclopediadearda.ui.theme.LightGray
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGolden07
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray08
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun SearchRoute(
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    SearchScreen(
        query = viewModel.query,
        uiState = uiState,
        onRetry = viewModel::retry,
        onCharacterClick = { id ->
            focusManager.clearFocus()
            keyboardController?.hide()
            onCharacterClick(id)
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: TextFieldState,
    uiState: SearchUiState,
    onRetry: () -> Unit,
    onCharacterClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = modifier
            .imePadding()
            .padding(4.dp)
    ) {
        Text(
            text = stringResource(R.string.search_title),
            style = MaterialTheme.typography.titleLarge,
            color = TolkienGold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .semantics { heading() }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .background(TolkienGray08, RectangleShape)
        ) {
            TextField(
                state = query,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = MaterialTheme.typography.bodyLarge,
                lineLimits = TextFieldLineLimits.SingleLine,
                shape = RectangleShape,
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_placeholder),
                        style = MaterialTheme.typography.bodyLarge,
                        color = LightGray.copy(alpha = 0.7f)
                    )
                },
                trailingIcon = {
                    if (query.text.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query.edit {
                                    replace(0, length, "")
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_close),
                                contentDescription = stringResource(
                                    R.string.clean_search
                                ),
                                tint = TolkienWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = TolkienWhite,
                    unfocusedTextColor = TolkienWhite,
                    cursorColor = TolkienGold,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = TolkienGolden07,
                    unfocusedIndicatorColor = TolkienGolden07.copy(alpha = 0.4f)
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search,
                    autoCorrectEnabled = false
                ),
                onKeyboardAction = {
                    keyboardController?.hide()
                }
            )
        }

        Spacer(Modifier.height(8.dp))

        AnimatedContent(
            targetState = uiState,
            contentKey = { it::class },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            transitionSpec = {
                fadeIn(tween(300)) togetherWith fadeOut(tween(300))
            },
            label = "searchState"
        ) { state ->
            when (state) {
                SearchUiState.Idle -> {
                    SearchFeedback(
                        title = stringResource(R.string.search_idle_title),
                        message = stringResource(R.string.search_hint)
                    )
                }

                SearchUiState.Searching -> {
                    SearchFeedback(
                        title = stringResource(R.string.search_loading),
                        showLoading = true
                    )
                }

                SearchUiState.Error -> {
                    SearchFeedback(
                        title = stringResource(R.string.search_error_title),
                        message = stringResource(R.string.search_error),
                        onRetry = onRetry
                    )
                }

                is SearchUiState.Results -> {
                    if (state.characters.isEmpty()) {
                        SearchFeedback(
                            title = stringResource(R.string.search_empty_title),
                            message = stringResource(R.string.search_empty)
                        )
                    } else {
                        SearchResultsList(
                            characters = state.characters,
                            onCharacterClick = onCharacterClick
                        )
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun SearchFeedback(
    title: String,
    message: String? = null,
    showLoading: Boolean = false,
    onRetry: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showLoading) {
                LoadingIndicator()
            } else {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = null,
                    tint = TolkienGolden07,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = TolkienGolden07,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { heading() }
            )

            message?.let {
                Spacer(Modifier.height(16.dp))

                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TolkienWhite,
                    textAlign = TextAlign.Center
                )
            }

            onRetry?.let { retry ->
                Spacer(Modifier.height(16.dp))

                TextButton(onClick = retry) {
                    Text(
                        text = stringResource(R.string.retry),
                        color = TolkienGold
                    )
                }
            }
        }
    }
}