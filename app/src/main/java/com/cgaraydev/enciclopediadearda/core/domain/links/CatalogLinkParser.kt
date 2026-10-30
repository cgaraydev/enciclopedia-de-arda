package com.cgaraydev.enciclopediadearda.core.domain.links

import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.core.model.CatalogLink
import java.net.URI

object CatalogLinkParser {

    fun parse(value: String): CatalogLink? {
        val raw = value.trim()
        if (raw.isEmpty()) return null

        val uri = runCatching { URI(raw) }.getOrNull()
            ?: return null

        val scheme = uri.scheme ?: return null

        val articleType = ArticleType.entries.firstOrNull {
            it.value.equals(scheme, ignoreCase = true)
        }

        if (articleType != null) {
            if (!uri.isOpaque || uri.fragment != null) return null

            val id = uri.schemeSpecificPart
                ?.takeIf { it.isNotBlank() }
                ?: return null

            return CatalogLink.Article(
                ArticleReference(
                    type = articleType,
                    id = id
                )
            )
        }

        val isWebLink =
            scheme.equals("https", ignoreCase = true) ||
                    scheme.equals("http", ignoreCase = true)

        return if (isWebLink && !uri.host.isNullOrBlank()) {
            CatalogLink.External(raw)
        } else {
            null
        }
    }
}