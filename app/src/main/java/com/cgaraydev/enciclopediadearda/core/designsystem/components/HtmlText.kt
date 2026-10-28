package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextAlign
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienGold
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun HtmlText(
    html: String,
    onLinkClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val text = remember(html, onLinkClick) {
        AnnotatedString.fromHtml(
            htmlString = html,
            linkStyles = TextLinkStyles(
                style = SpanStyle(color = TolkienGold)
            ),
            linkInteractionListener = { link ->
                if (link is LinkAnnotation.Url) {
                    onLinkClick(link.url)
                }
            }
        )
    }

    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = TolkienWhite,
        textAlign = TextAlign.Start,
        modifier = modifier.fillMaxWidth()
    )
}