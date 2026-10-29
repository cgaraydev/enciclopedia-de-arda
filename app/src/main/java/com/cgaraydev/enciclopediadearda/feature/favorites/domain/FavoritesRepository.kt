package com.cgaraydev.enciclopediadearda.feature.favorites.domain

import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {

    fun observeIsFavorite(
        type: ArticleType,
        id: String
    ): Flow<Boolean>

    suspend fun setFavorite(
        type: ArticleType,
        id: String,
        isFavorite: Boolean
    )

    fun observeFavorites(): Flow<List<FavoriteReference>>
}