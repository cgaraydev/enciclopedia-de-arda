package com.cgaraydev.enciclopediadearda.core.model

interface CategoryItem {
    val id: String
    val name: String
    val imagePath: String?
    val tags: List<String>
    val articleType: ArticleType

    val reference: ArticleReference
        get() = ArticleReference(
            type = articleType,
            id = id
        )
}