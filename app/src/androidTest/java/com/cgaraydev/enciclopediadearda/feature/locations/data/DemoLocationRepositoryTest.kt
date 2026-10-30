package com.cgaraydev.enciclopediadearda.feature.locations.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoLocationRepositoryTest {

    @Test
    fun loadsPackagedDemoCatalogAndFindsLocations() = runBlocking {
        val context =
            InstrumentationRegistry.getInstrumentation().targetContext

        val repository = DemoLocationRepository(
            LocationAssetDataSource(
                context = context,
                decoder = LocationCatalogDecoder()
            )
        )

        val locations = repository.getLocations()

        assertEquals(
            setOf("demo-location-001", "demo-location-002"),
            locations.map { it.id }.toSet()
        )
        assertNotNull(
            repository.getLocationById("demo-location-001")
        )
        assertNull(
            repository.getLocationById("missing-location")
        )
    }
}