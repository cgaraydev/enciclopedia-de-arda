package com.cgaraydev.enciclopediadearda.core.images

object CatalogImageUrls {

    private const val BASE_URL =
        "https://enciclopediadearda-repo.github.io/app-images/"

    fun resolve(path: String?): String? =
        path
            ?.trim()
            ?.takeIf { it.isNotEmpty() }
            ?.let { BASE_URL + it.trimStart('/') }
}