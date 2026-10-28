package com.cgaraydev.enciclopediadearda.feature.characters.domain

data class Character(
    val id: String,
    val name: String,
    val imagePath: String?,
    val biographyHtml: String?,
    val otherNames: String? = null,
    val birth: String? = null,
    val death: String? = null,
    val titles: String? = null,
    val love: String? = null,
    val family: String? = null,
    val house: String? = null,
    val etymologyHtml: String? = null
)