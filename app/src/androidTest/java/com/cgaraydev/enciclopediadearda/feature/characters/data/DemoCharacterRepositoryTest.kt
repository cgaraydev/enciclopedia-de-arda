package com.cgaraydev.enciclopediadearda.feature.characters.data

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DemoCharacterRepositoryTest {

    @Test
    fun readsPackagedCatalogAndFindsCharacters() {
        runBlocking {
            val context = InstrumentationRegistry
                .getInstrumentation()
                .targetContext

            val repository = DemoCharacterRepository(
                dataSource = CharacterAssetDataSource(
                    context = context,
                    decoder = CharacterCatalogDecoder()
                )
            )

            val characters = repository.getCharacters()

            assertEquals(
                setOf("demo-001", "demo-002"),
                characters.map { it.id }.toSet()
            )
            assertEquals(
                "Exploradora de demostración",
                repository.getCharacterById("demo-001")?.name
            )
            assertTrue(
                repository.getCharacterById("demo-001")
                    ?.biographyHtml
                    ?.contains("character:demo-002") == true
            )
            assertNull(repository.getCharacterById("missing-character"))
        }
    }
}