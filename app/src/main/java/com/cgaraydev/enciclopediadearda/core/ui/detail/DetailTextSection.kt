package com.cgaraydev.enciclopediadearda.core.ui.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.core.designsystem.components.CategoriesHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.HtmlText

@Composable
fun DetailTextSection(
    @StringRes titleRes: Int,
    html: String?,
    onLinkClick: (String) -> Unit
) {
    if (html.isNullOrBlank()) return

    CategoriesHeader(textRes = titleRes)

    HtmlText(
        html = html,
        onLinkClick = onLinkClick,
        modifier = Modifier.padding(horizontal = 16.dp)
    )

    Spacer(Modifier.height(20.dp))
}