package com.cgaraydev.enciclopediadearda.feature.home.presentation

sealed interface HomeUiState {
    data object Loading : HomeUiState

    data class Content(
        val characterIds: List<String>
    ) : HomeUiState

    data object Error : HomeUiState
}