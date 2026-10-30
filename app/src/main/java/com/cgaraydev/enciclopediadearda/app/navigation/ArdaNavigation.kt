package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.cgaraydev.enciclopediadearda.feature.characters.presentation.CharactersRoute
import com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail.CharacterDetailRoute
import com.cgaraydev.enciclopediadearda.feature.favorites.presentation.FavoritesRoute
import com.cgaraydev.enciclopediadearda.feature.home.presentation.HomeRoute
import com.cgaraydev.enciclopediadearda.feature.search.presentation.SearchRoute
import com.cgaraydev.enciclopediadearda.feature.calendar.presentation.CalendarRoute
import com.cgaraydev.enciclopediadearda.feature.locations.presentation.LocationsRoute
import com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail.LocationDetailRoute
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import androidx.compose.ui.platform.LocalUriHandler
import com.cgaraydev.enciclopediadearda.core.domain.links.CatalogLinkParser
import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.CatalogLink
import com.cgaraydev.enciclopediadearda.feature.home.presentation.HomeNavigationAction

@Composable
fun ArdaNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HomeDestination)
    val uriHandler = LocalUriHandler.current

    val navigateTo: (NavKey) -> Unit = { destination ->
        val existingIndex = backStack.indexOf(destination)

        if (existingIndex >= 0) {
            while (backStack.lastIndex > existingIndex) {
                backStack.removeAt(backStack.lastIndex)
            }
        } else {
            val isMainDestination =
                destination == HomeDestination ||
                        destination == CharactersDestination ||
                        destination == LocationsDestination ||
                        destination == FavoritesDestination ||
                        destination == CalendarDestination

            if (isMainDestination) {
                while (backStack.size > 1) {
                    backStack.removeAt(backStack.lastIndex)
                }
            }

            backStack.add(destination)
        }
    }

    val goBack: () -> Unit = {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.lastIndex)
        }
    }

    val openArticle: (ArticleReference) -> Unit = { reference ->
        navigateTo(reference.detailDestination())
    }

    val openCharacter: (String) -> Unit = { id ->
        openArticle(ArticleReference(ArticleType.CHARACTER, id))
    }

    val openLocation: (String) -> Unit = { id ->
        openArticle(ArticleReference(ArticleType.LOCATION, id))
    }

    val openLink: (String) -> Unit = { value ->
        when (val link = CatalogLinkParser.parse(value)) {
            is CatalogLink.Article -> openArticle(link.reference)
            is CatalogLink.External -> uriHandler.openUri(link.url)
            null -> Unit
        }
    }

    val selectedDestination = when (backStack.lastOrNull()) {
        is CharacterDetailDestination -> CharactersDestination
        is LocationDetailDestination -> LocationsDestination
        CharactersDestination -> CharactersDestination
        LocationsDestination -> LocationsDestination
        CalendarDestination -> CalendarDestination
        FavoritesDestination -> FavoritesDestination
        HomeDestination -> HomeDestination
        else -> null
    }

    ArdaAppScaffold(
        selectedDestination = selectedDestination,
        canGoBack = backStack.size > 1,
        onBack = goBack,
        onNavigate = navigateTo,
        modifier = modifier
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
            backStack = backStack,
            onBack = goBack,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<CharactersDestination> {
                    CharactersRoute(
                        onCharacterClick = openCharacter,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<CharacterDetailDestination> { destination ->
                    CharacterDetailRoute(
                        characterId = destination.characterId,
                        onLinkClick = openLink,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<FavoritesDestination> {
                    FavoritesRoute(
                        onArticleClick = { type, id ->
                            openArticle(ArticleReference(type, id))
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<HomeDestination> {
                    HomeRoute(
                        onNavigate = { action ->
                            when (action) {
                                is HomeNavigationAction.OpenArticle -> {
                                    openArticle(action.reference)
                                }

                                is HomeNavigationAction.OpenSection -> {
                                    navigateTo(action.type.sectionDestination())
                                }

                                is HomeNavigationAction.OpenLink -> {
                                    openLink(action.url)
                                }

                                HomeNavigationAction.OpenSearch -> {
                                    navigateTo(SearchDestination)
                                }

                                HomeNavigationAction.OpenCalendar -> {
                                    navigateTo(CalendarDestination)
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<SearchDestination> {
                    SearchRoute(
                        onCharacterClick = openCharacter,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<CalendarDestination> {
                    CalendarRoute(
                        onLinkClick = openLink,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<LocationsDestination> {
                    LocationsRoute(
                        onLocationClick = openLocation,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<LocationDetailDestination> { destination ->
                    LocationDetailRoute(
                        locationId = destination.locationId,
                        onLinkClick = openLink,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}