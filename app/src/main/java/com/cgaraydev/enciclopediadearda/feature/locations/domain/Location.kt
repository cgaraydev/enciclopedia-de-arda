package com.cgaraydev.enciclopediadearda.feature.locations.domain

import com.cgaraydev.enciclopediadearda.core.model.CatalogExternalLink
import com.cgaraydev.enciclopediadearda.core.model.CatalogImage

data class Location(
    val id: String,
    val name: String,
    val imagePath: String? = null,
    val capital: String? = null,
    val inhabitants: String? = null,
    val governance: String? = null,
    val languages: String? = null,
    val founded: String? = null,
    val destroyed: String? = null,
    val otherNames: String? = null,
    val typeHtml: String? = null,
    val events: String? = null,
    val location: String? = null,
    val historyHtml: String? = null,
    val majorTowns: String? = null,
    val regions: String? = null,
    val etymologyHtml: String? = null,
    val images: List<CatalogImage> = emptyList(),
    val externalLinks: List<CatalogExternalLink> = emptyList(),
    val tags: List<String> = emptyList()
)