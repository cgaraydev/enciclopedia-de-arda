package com.cgaraydev.enciclopediadearda.feature.characters.presentation

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
class CharactersViewModelTest {

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
    fun loadsCharacters() = runTest {
        val characters = listOf(
            Character(
                id = "demo-001",
                name = "Exploradora",
                imagePath = null,
                biographyHtml = null
            )
        )
        val repository = FakeCharacterRepository(characters)
        val viewModel = CharactersViewModel(repository)

        assertEquals(CharactersUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        assertEquals(
            CharactersUiState.Content(characters),
            viewModel.uiState.value
        )
    }

    @Test
    fun retriesAfterLoadFailure() = runTest {
        val repository = FakeCharacterRepository(emptyList())
        repository.shouldFail = true
        val viewModel = CharactersViewModel(repository)

        advanceUntilIdle()

        assertEquals(CharactersUiState.Error, viewModel.uiState.value)

        repository.shouldFail = false
        viewModel.retry()

        assertEquals(CharactersUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        assertEquals(
            CharactersUiState.Content(emptyList()),
            viewModel.uiState.value
        )
        assertEquals(2, repository.loadCount)
    }

    @Test
    fun ignoresRetryWhileLoading() = runTest {
        val repository = FakeCharacterRepository(emptyList())
        val viewModel = CharactersViewModel(repository)

        viewModel.retry()
        viewModel.retry()

        advanceUntilIdle()

        assertEquals(1, repository.loadCount)
    }

    private class FakeCharacterRepository(
        private val characters: List<Character>
    ) : CharacterRepository {

        var shouldFail = false
        var loadCount = 0

        override suspend fun getCharacters(): List<Character> {
            loadCount++
            if (shouldFail) throw IOException("Test load failure")
            return characters
        }

        override suspend fun getCharacterById(id: String): Character? =
            characters.firstOrNull { it.id == id }
    }
}