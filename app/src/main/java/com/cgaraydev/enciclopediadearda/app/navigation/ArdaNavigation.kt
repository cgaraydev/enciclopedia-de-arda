package com.cgaraydev.enciclopediadearda.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.cgaraydev.enciclopediadearda.feature.characters.presentation.CharactersRoute
import com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail.CharacterDetailRoute
import androidx.compose.foundation.layout.fillMaxSize
import com.cgaraydev.enciclopediadearda.feature.favorites.presentation.FavoritesRoute

@Composable
fun ArdaNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(CharactersDestination)

    val openCharacter: (String) -> Unit = { id ->
        val destination = CharacterDetailDestination(id)
        val existingIndex = backStack.indexOf(destination)

        if (existingIndex >= 0) {
            while (backStack.lastIndex > existingIndex) {
                backStack.removeAt(backStack.lastIndex)
            }
        } else {
            backStack.add(destination)
        }
    }

    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<CharactersDestination> {
                CharactersRoute(
                    onCharacterClick = openCharacter,
                    onFavoritesClick = {
                        if (backStack.lastOrNull() != FavoritesDestination) {
                            backStack.add(FavoritesDestination)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            entry<CharacterDetailDestination> { destination ->
                CharacterDetailRoute(
                    characterId = destination.characterId,
                    onCharacterClick = openCharacter,
                    modifier = Modifier.fillMaxSize()
                )
            }

            entry<FavoritesDestination> {
                FavoritesRoute(
                    onCharacterClick = openCharacter,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    )
}