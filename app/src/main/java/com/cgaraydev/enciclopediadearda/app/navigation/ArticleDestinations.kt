package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.navigation3.runtime.NavKey
import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType

fun ArticleReference.detailDestination(): NavKey =
    when (type) {
        ArticleType.CHARACTER -> CharacterDetailDestination(id)
        ArticleType.LOCATION -> LocationDetailDestination(id)
    }

fun ArticleType.sectionDestination(): NavKey =
    when (this) {
        ArticleType.CHARACTER -> CharactersDestination
        ArticleType.LOCATION -> LocationsDestination
    }