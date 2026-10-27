package com.cgaraydev.enciclopediadearda.feature.characters.domain

data class Character(
    val id: String,
    val name: String,
    val imagePath: String?,
    val biographyHtml: String?
)