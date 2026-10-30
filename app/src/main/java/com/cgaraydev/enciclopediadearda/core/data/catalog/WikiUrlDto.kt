package com.cgaraydev.enciclopediadearda.core.data.catalog

import com.cgaraydev.enciclopediadearda.core.model.CatalogExternalLink
import com.cgaraydev.enciclopediadearda.core.model.CatalogLinkSource
import java.net.URI
import kotlinx.serialization.Serializable

@Serializable
internal data class WikiUrlDto(
    val tolkienGateway: String? = null,
    val lotrFandom: String? = null,
    val lotrFandomEs: String? = null,
    val wikipedia: String? = null,
    val elFenomeno: String? = null,
    val website: String? = null,
    val imdb: String? = null
) {
    fun toDomain(): List<CatalogExternalLink> {
        val candidates = listOf(
            CatalogLinkSource.TOLKIEN_GATEWAY to tolkienGateway,
            CatalogLinkSource.LOTR_FANDOM_EN to lotrFandom,
            CatalogLinkSource.LOTR_FANDOM_ES to lotrFandomEs,
            CatalogLinkSource.WIKIPEDIA to wikipedia,
            CatalogLinkSource.EL_FENOMENO to elFenomeno,
            CatalogLinkSource.WEBSITE to website,
            CatalogLinkSource.IMDB to imdb
        )

        return candidates.mapNotNull { (source, rawUrl) ->
            val url = rawUrl?.trim()
                ?.takeIf { it.isNotEmpty() }
                ?: return@mapNotNull null

            val uri = runCatching { URI(url) }.getOrNull()
                ?: return@mapNotNull null

            val supportedScheme =
                uri.scheme.equals("https", ignoreCase = true) ||
                        uri.scheme.equals("http", ignoreCase = true)

            if (supportedScheme && !uri.host.isNullOrBlank()) {
                CatalogExternalLink(source = source, url = url)
            } else {
                null
            }
        }
    }
}