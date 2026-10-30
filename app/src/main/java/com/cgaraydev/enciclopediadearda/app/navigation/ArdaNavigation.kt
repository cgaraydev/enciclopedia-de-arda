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

@Composable
fun ArdaNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HomeDestination)

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

    val openCharacter: (String) -> Unit = { id ->
        navigateTo(CharacterDetailDestination(id))
    }

    val openLocation: (String) -> Unit = { id ->
        navigateTo(LocationDetailDestination(id))
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
                        onCharacterClick = openCharacter,
                        onLocationClick = openLocation,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<FavoritesDestination> {
                    FavoritesRoute(
                        onArticleClick = { type, id ->
                            when (type) {
                                ArticleType.CHARACTER -> openCharacter(id)
                                ArticleType.LOCATION -> openLocation(id)
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                entry<HomeDestination> {
                    HomeRoute(
                        onCharactersClick = {
                            navigateTo(CharactersDestination)
                        },
                        onCharacterClick = openCharacter,
                        onLocationClick = openLocation,
                        onSearchClick = {
                            navigateTo(SearchDestination)
                        },
                        onCalendarClick = {
                            navigateTo(CalendarDestination)
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
                        onCharacterClick = openCharacter,
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
                        onLocationClick = openLocation,
                        onCharacterClick = openCharacter,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        )
    }
}