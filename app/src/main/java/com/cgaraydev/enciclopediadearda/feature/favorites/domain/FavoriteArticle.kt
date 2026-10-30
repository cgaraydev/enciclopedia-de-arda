package com.cgaraydev.enciclopediadearda.feature.favorites.domain

import com.cgaraydev.enciclopediadearda.core.model.ArticleType

data class FavoriteArticle(
    val type: ArticleType,
    val id: String,
    val name: String
)