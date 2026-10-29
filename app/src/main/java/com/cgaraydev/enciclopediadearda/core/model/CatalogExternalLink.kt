package com.cgaraydev.enciclopediadearda.core.model

data class CatalogExternalLink(
    val source: CatalogLinkSource,
    val url: String
)

enum class CatalogLinkSource {
    TOLKIEN_GATEWAY,
    LOTR_FANDOM_EN,
    LOTR_FANDOM_ES,
    WIKIPEDIA,
    EL_FENOMENO,
    WEBSITE,
    IMDB
}