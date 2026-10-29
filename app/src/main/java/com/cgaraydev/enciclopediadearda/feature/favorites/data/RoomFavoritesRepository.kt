package com.cgaraydev.enciclopediadearda.feature.favorites.data

import com.cgaraydev.enciclopediadearda.core.database.FavoriteDao
import com.cgaraydev.enciclopediadearda.core.database.FavoriteEntity
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.favorites.domain.FavoritesRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class RoomFavoritesRepository @Inject constructor(
    private val dao: FavoriteDao
) : FavoritesRepository {

    override fun observeIsFavorite(
        type: ArticleType,
        id: String
    ): Flow<Boolean> = dao.observeIsFavorite(type.value, id)

    override suspend fun setFavorite(
        type: ArticleType,
        id: String,
        isFavorite: Boolean
    ) {
        require(id.isNotBlank())

        if (isFavorite) {
            dao.insert(
                FavoriteEntity(
                    articleType = type.value,
                    articleId = id
                )
            )
        } else {
            dao.delete(type.value, id)
        }
    }
}