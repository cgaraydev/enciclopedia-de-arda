package com.cgaraydev.enciclopediadearda.core.data.catalog

import com.cgaraydev.enciclopediadearda.core.model.CatalogImage
import kotlinx.serialization.Serializable

@Serializable
internal data class CatalogImageDto(
    val url: String? = null,
    val artist: String? = null
) {
    fun toDomain(): CatalogImage = CatalogImage(
        imagePath = url,
        artistHtml = artist
    )
}