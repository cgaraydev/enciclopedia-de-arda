package com.cgaraydev.enciclopediadearda.feature.home.presentation

import com.cgaraydev.enciclopediadearda.core.model.ArticleReference
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun includesBothCategoriesEvenWhenIdsMatch() = runTest {
        val viewModel = createViewModel(
            characters = listOf(
                Character(
                    id = "shared-id",
                    name = "Personaje",
                    imagePath = null,
                    biographyHtml = null
                )
            ),
            locations = listOf(
                Location(id = "shared-id", name = "Lugar")
            )
        )

        advanceUntilIdle()

        val content = viewModel.uiState.value as HomeUiState.Content

        assertEquals(
            listOf(
                ArticleReference(ArticleType.CHARACTER, "shared-id"),
                ArticleReference(ArticleType.LOCATION, "shared-id")
            ),
            content.articleReferences
        )
    }

    @Test
    fun supportsLocationsWhenCharacterCatalogIsEmpty() = runTest {
        val viewModel = createViewModel(
            characters = emptyList(),
            locations = listOf(
                Location(id = "location", name = "Lugar")
            )
        )

        advanceUntilIdle()

        val content = viewModel.uiState.value as HomeUiState.Content

        assertEquals(
            listOf(
                ArticleReference(ArticleType.LOCATION, "location")
            ),
            content.articleReferences
        )
        assertNull(content.featuredCharacter)
    }

    private fun createViewModel(
        characters: List<Character>,
        locations: List<Location>
    ): HomeViewModel = HomeViewModel(
        characterRepository = object : CharacterRepository {
            override suspend fun getCharacters(): List<Character> =
                characters

            override suspend fun getCharacterById(id: String): Character? =
                characters.firstOrNull { it.id == id }
        },
        locationRepository = object : LocationRepository {
            override suspend fun getLocations(): List<Location> =
                locations

            override suspend fun getLocationById(id: String): Location? =
                locations.firstOrNull { it.id == id }
        }
    )
}