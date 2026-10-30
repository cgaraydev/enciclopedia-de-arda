package com.cgaraydev.enciclopediadearda.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val characterRepository: CharacterRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadCatalog()
    }

    fun retry() {
        if (_uiState.value == HomeUiState.Error) {
            loadCatalog()
        }
    }

    private fun loadCatalog() {
        _uiState.value = HomeUiState.Loading

        viewModelScope.launch {
            try {
                val characters = characterRepository.getCharacters()
                val locations = locationRepository.getLocations()

                val references = buildList {
                    characters.forEach {
                        add(
                            ArticleReference(
                                type = ArticleType.CHARACTER,
                                id = it.id
                            )
                        )
                    }

                    locations.forEach {
                        add(
                            ArticleReference(
                                type = ArticleType.LOCATION,
                                id = it.id
                            )
                        )
                    }
                }

                _uiState.value = HomeUiState.Content(
                    articleReferences = references,
                    featuredCharacter = characters.randomOrNull()
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (_: Exception) {
                _uiState.value = HomeUiState.Error
            }
        }
    }
}