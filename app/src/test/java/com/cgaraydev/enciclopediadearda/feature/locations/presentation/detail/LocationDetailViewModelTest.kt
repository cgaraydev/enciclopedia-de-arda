package com.cgaraydev.enciclopediadearda.feature.locations.presentation.detail

import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
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
import com.cgaraydev.enciclopediadearda.core.ui.detail.DetailUiState

@OptIn(ExperimentalCoroutinesApi::class)
class LocationDetailViewModelTest {
    
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
    fun loadsSelectedLocation() = runTest {
        val repository = FakeLocationRepository()
        val viewModel = LocationDetailViewModel(repository)

        viewModel.loadLocation("demo-001")

        assertEquals(
            DetailUiState.Loading,
            viewModel.uiState.value
        )

        advanceUntilIdle()

        assertEquals(
            DetailUiState.Content(repository.location),
            viewModel.uiState.value
        )
    }

    @Test
    fun reportsMissingLocation() = runTest {
        val viewModel = LocationDetailViewModel(
            FakeLocationRepository()
        )

        viewModel.loadLocation("missing")
        advanceUntilIdle()

        assertEquals(
            DetailUiState.NotFound,
            viewModel.uiState.value
        )
    }

    @Test
    fun retriesFailedLoad() = runTest {
        val repository = FakeLocationRepository()
        repository.shouldFail = true
        val viewModel = LocationDetailViewModel(repository)

        viewModel.loadLocation("demo-001")
        advanceUntilIdle()

        assertEquals(
            DetailUiState.Error,
            viewModel.uiState.value
        )

        repository.shouldFail = false
        viewModel.retry()
        advanceUntilIdle()

        assertEquals(
            DetailUiState.Content(repository.location),
            viewModel.uiState.value
        )
        assertEquals(2, repository.detailRequests)
    }

    @Test
    fun avoidsReloadingSameLocation() = runTest {
        val repository = FakeLocationRepository()
        val viewModel = LocationDetailViewModel(repository)

        viewModel.loadLocation("demo-001")
        advanceUntilIdle()

        viewModel.loadLocation("demo-001")
        advanceUntilIdle()

        assertEquals(1, repository.detailRequests)
    }

    private class FakeLocationRepository : LocationRepository {

        val location = Location(
            id = "demo-001",
            name = "Exploradora de demostración",
            imagePath = null,
            historyHtml = "<strong>Lugar ficticio</strong>"
        )

        var shouldFail = false
        var detailRequests = 0

        override suspend fun getLocations(): List<Location> =
            listOf(location)

        override suspend fun getLocationById(id: String): Location? {
            detailRequests++

            if (shouldFail) {
                throw IOException("Test load failure")
            }

            return location.takeIf { it.id == id }
        }
    }
}