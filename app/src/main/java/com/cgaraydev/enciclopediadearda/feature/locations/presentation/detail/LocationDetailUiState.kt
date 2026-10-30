package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location

sealed interface LocationDetailUiState {
    data object Loading : LocationDetailUiState
    data object Error : LocationDetailUiState
    data object NotFound : LocationDetailUiState
    data class Content(val location: Location) : LocationDetailUiState
}