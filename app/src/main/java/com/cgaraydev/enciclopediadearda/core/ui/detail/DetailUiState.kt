package com.cgaraydev.enciclopediadearda.core.ui.detail

sealed interface DetailUiState<out T> {
    data object Loading : DetailUiState<Nothing>
    data object Error : DetailUiState<Nothing>
    data object NotFound : DetailUiState<Nothing>
    data class Content<T>(val item: T) : DetailUiState<T>
}