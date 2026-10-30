package com.cgaraydev.enciclopediadearda.feature.search.presentation

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import com.cgaraydev.enciclopediadearda.feature.search.domain.CharacterSearchMatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    val query = TextFieldState()

    private val retryRequests = MutableStateFlow(0)

    private val _uiState =
        MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                snapshotFlow { query.text.toString() },
                retryRequests
            ) { text, _ -> text }
                .collectLatest { text ->
                    val normalizedQuery =
                        CharacterSearchMatcher.normalize(text)

                    if (normalizedQuery.length < 2) {
                        _uiState.value = SearchUiState.Idle
                        return@collectLatest
                    }

                    _uiState.value = SearchUiState.Searching
                    delay(300)

                    try {
                        val characters = repository.getCharacters()

                        val results = withContext(Dispatchers.Default) {
                            CharacterSearchMatcher.search(
                                characters = characters,
                                query = normalizedQuery
                            )
                        }

                        currentCoroutineContext().ensureActive()
                        _uiState.value = SearchUiState.Results(results)
                    } catch (exception: CancellationException) {
                        throw exception
                    } catch (_: Exception) {
                        _uiState.value = SearchUiState.Error
                    }
                }
        }
    }

    fun retry() {
        if (_uiState.value == SearchUiState.Error) {
            retryRequests.update { it + 1 }
        }
    }
}