package com.cgaraydev.enciclopediadearda.core.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cgaraydev.enciclopediadearda.core.designsystem.components.ArticleDetailHeader
import com.cgaraydev.enciclopediadearda.core.designsystem.components.DetailImage
import com.cgaraydev.enciclopediadearda.core.model.CategoryItem

@Composable
fun ArticleDetailLayout(
    item: CategoryItem,
    onLinkClick: (String) -> Unit,
    favoriteAction: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    categoryHtml: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val scrollState = rememberScrollState()

    Column(modifier = modifier.fillMaxSize()) {
        ArticleDetailHeader(
            title = item.name,
            scrollState = scrollState,
            categoryHtml = categoryHtml,
            onLinkClick = onLinkClick
        )

        Spacer(Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            DetailImage(
                imagePath = item.imagePath,
                detailName = item.name
            )

            favoriteAction()

            Spacer(Modifier.height(20.dp))

            content()
        }
    }
}