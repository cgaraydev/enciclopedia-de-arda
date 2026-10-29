package com.cgaraydev.enciclopediadearda.feature.favorites.domain

import com.cgaraydev.enciclopediadearda.core.model.ArticleType

data class FavoriteReference(
    val type: ArticleType,
    val id: String
)