package com.cgaraydev.enciclopediadearda.feature.search.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.core.DataStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import java.io.File
import java.util.UUID
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStoreSearchHistoryRepositoryTest {

    private lateinit var file: File
    private lateinit var storeJob: CompletableJob
    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: DataStoreSearchHistoryRepository

    @Before
    fun setUp() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        file = File(
            context.cacheDir,
            "history-test-${UUID.randomUUID()}.preferences_pb"
        )
        openStore()
    }

    @After
    fun tearDown() {
        runBlocking {
            storeJob.cancelAndJoin()
        }
        file.delete()
    }

    @Test
    fun repeatedQueryMovesToFrontWithoutDuplicates() = runBlocking {
        repository.record("Demostración")
        repository.record("Cronista")
        repository.record("  DEMOSTRACION  ")

        assertEquals(
            listOf("DEMOSTRACION", "Cronista"),
            repository.observeHistory().first()
        )
    }

    @Test
    fun keepsOnlyTenMostRecentQueries() = runBlocking {
        repeat(12) { repository.record("consulta $it") }

        assertEquals(
            (11 downTo 2).map { "consulta $it" },
            repository.observeHistory().first()
        )
    }

    @Test
    fun clearsHistory() = runBlocking {
        repository.record("Exploradora")
        repository.clear()

        assertTrue(repository.observeHistory().first().isEmpty())
    }

    @Test
    fun restoresHistoryAfterReopeningStore() = runBlocking {
        repository.record("Cronista")

        storeJob.cancelAndJoin()
        openStore()

        assertEquals(
            listOf("Cronista"),
            repository.observeHistory().first()
        )
    }

    private fun openStore() {
        storeJob = SupervisorJob()

        dataStore = PreferenceDataStoreFactory.create(
            scope = CoroutineScope(storeJob + Dispatchers.IO),
            produceFile = { file }
        )

        repository = DataStoreSearchHistoryRepository(dataStore)
    }
}