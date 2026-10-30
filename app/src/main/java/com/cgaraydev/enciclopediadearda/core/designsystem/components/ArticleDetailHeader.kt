package com.cgaraydev.enciclopediadearda.core.designsystem.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.ui.theme.TolkienWhite

@Composable
fun ArticleDetailHeader(
    title: String,
    scrollState: ScrollState,
    onLinkClick: (String) -> Unit,
    categoryHtml: String? = null
) {
    val isCollapsed by remember(scrollState) {
        derivedStateOf { scrollState.value > 50 }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .animateContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title.lowercase(),
            style = if (isCollapsed) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.displaySmall
            },
            color = TolkienWhite,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { heading() }
        )

        categoryHtml?.takeIf { it.isNotBlank() }?.let {
            HtmlText(
                html = it,
                onLinkClick = onLinkClick,
                textAlign = TextAlign.Center
            )
        }
    }
}