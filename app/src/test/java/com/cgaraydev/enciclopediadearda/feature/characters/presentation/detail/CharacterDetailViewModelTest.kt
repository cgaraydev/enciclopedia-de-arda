package com.cgaraydev.enciclopediadearda.feature.characters.presentation.detail

import com.cgaraydev.enciclopediadearda.feature.characters.domain.Character
import com.cgaraydev.enciclopediadearda.feature.characters.domain.CharacterRepository
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

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
    fun loadsSelectedCharacter() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = CharacterDetailViewModel(repository)

        viewModel.loadCharacter("demo-001")

        assertEquals(
            CharacterDetailUiState.Loading,
            viewModel.uiState.value
        )

        advanceUntilIdle()

        assertEquals(
            CharacterDetailUiState.Content(repository.character),
            viewModel.uiState.value
        )
    }

    @Test
    fun reportsMissingCharacter() = runTest {
        val viewModel = CharacterDetailViewModel(
            FakeCharacterRepository()
        )

        viewModel.loadCharacter("missing")
        advanceUntilIdle()

        assertEquals(
            CharacterDetailUiState.NotFound,
            viewModel.uiState.value
        )
    }

    @Test
    fun retriesFailedLoad() = runTest {
        val repository = FakeCharacterRepository()
        repository.shouldFail = true
        val viewModel = CharacterDetailViewModel(repository)

        viewModel.loadCharacter("demo-001")
        advanceUntilIdle()

        assertEquals(
            CharacterDetailUiState.Error,
            viewModel.uiState.value
        )

        repository.shouldFail = false
        viewModel.retry()
        advanceUntilIdle()

        assertEquals(
            CharacterDetailUiState.Content(repository.character),
            viewModel.uiState.value
        )
        assertEquals(2, repository.detailRequests)
    }

    @Test
    fun avoidsReloadingSameCharacter() = runTest {
        val repository = FakeCharacterRepository()
        val viewModel = CharacterDetailViewModel(repository)

        viewModel.loadCharacter("demo-001")
        advanceUntilIdle()

        viewModel.loadCharacter("demo-001")
        advanceUntilIdle()

        assertEquals(1, repository.detailRequests)
    }

    private class FakeCharacterRepository : CharacterRepository {

        val character = Character(
            id = "demo-001",
            name = "Exploradora de demostración",
            imagePath = null,
            biographyHtml = "<strong>Personaje ficticio</strong>"
        )

        var shouldFail = false
        var detailRequests = 0

        override suspend fun getCharacters(): List<Character> =
            listOf(character)

        override suspend fun getCharacterById(id: String): Character? {
            detailRequests++

            if (shouldFail) {
                throw IOException("Test load failure")
            }

            return character.takeIf { it.id == id }
        }
    }
}