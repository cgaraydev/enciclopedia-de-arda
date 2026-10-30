package com.cgaraydev.enciclopediadearda.feature.home.presentation

import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType

sealed interface HomeNavigationAction {

    data class OpenArticle(
        val reference: ArticleReference
    ) : HomeNavigationAction

    data class OpenSection(
        val type: ArticleType
    ) : HomeNavigationAction

    data class OpenLink(
        val url: String
    ) : HomeNavigationAction

    data object OpenSearch : HomeNavigationAction

    data object OpenCalendar : HomeNavigationAction
}