package com.cgaraydev.enciclopediadearda.core.model

sealed interface CatalogLink {

    data class Article(
        val reference: ArticleReference
    ) : CatalogLink

    data class External(
        val url: String
    ) : CatalogLink
}