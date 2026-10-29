package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.R
import com.cgaraydev.enciclopediadearda.core.model.CatalogExternalLink
import com.cgaraydev.enciclopediadearda.core.model.CatalogLinkSource
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGray07
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun WikiLinksSection(
    links: List<CatalogExternalLink>,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (links.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        CategoriesHeader(textRes = R.string.external_links)
        Spacer(Modifier.height(8.dp))

        links.forEachIndexed { index, link ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(role = Role.Button) {
                        onLinkClick(link.url)
                    }
                    .heightIn(min = 48.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_link),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = TolkienWhite
                )

                Spacer(Modifier.width(20.dp))

                Text(
                    text = stringResource(link.source.labelRes()).lowercase(),
                    style = MaterialTheme.typography.titleSmall,
                    color = TolkienWhite,
                    modifier = Modifier.weight(1f)
                )
            }

            if (index < links.lastIndex) {
                HorizontalDivider(
                    color = TolkienGray07,
                    thickness = 0.5.dp,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(Modifier.height(20.dp))
    }
}

@StringRes
private fun CatalogLinkSource.labelRes(): Int = when (this) {
    CatalogLinkSource.TOLKIEN_GATEWAY -> R.string.wiki_tolkien_gateway
    CatalogLinkSource.LOTR_FANDOM_EN -> R.string.wiki_lotr_fandom_en
    CatalogLinkSource.LOTR_FANDOM_ES -> R.string.wiki_lotr_fandom_es
    CatalogLinkSource.WIKIPEDIA -> R.string.wiki_wikipedia
    CatalogLinkSource.EL_FENOMENO -> R.string.wiki_el_fenomeno
    CatalogLinkSource.WEBSITE -> R.string.wiki_website
    CatalogLinkSource.IMDB -> R.string.wiki_imdb
}