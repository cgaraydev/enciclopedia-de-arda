package com.cgaraydev.enciclopediadearda.feature.favorites.data

import android.content.Context
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.cgaraydev.enciclopediadearda.core.database.ArdaDatabase
import com.cgaraydev.enciclopediadearda.core.database.FavoriteEntity
import com.cgaraydev.enciclopediadearda.core.model.ArticleType
import java.util.UUID
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.cgaraydev.enciclopediadearda.feature.favorites.domain.FavoriteReference

@RunWith(AndroidJUnit4::class)
class RoomFavoritesRepositoryTest {

    private lateinit var context: Context
    private lateinit var databaseName: String
    private lateinit var database: ArdaDatabase
    private lateinit var repository: RoomFavoritesRepository

    @Before
    fun setUp() {
        context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
        databaseName = "favorites-test-${UUID.randomUUID()}.db"
        openDatabase()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    @Test
    fun addsAndRemovesFavorite() = runBlocking {
        val type = ArticleType.CHARACTER
        val id = "demo-001"

        assertFalse(repository.observeIsFavorite(type, id).first())

        repository.setFavorite(type, id, true)
        assertTrue(repository.observeIsFavorite(type, id).first())

        repository.setFavorite(type, id, false)
        assertFalse(repository.observeIsFavorite(type, id).first())
    }

    @Test
    fun repeatedSaveDoesNotCreateDuplicates() = runBlocking {
        repository.setFavorite(ArticleType.CHARACTER, "demo-001", true)
        repository.setFavorite(ArticleType.CHARACTER, "demo-001", true)

        assertEquals(
            1,
            database.favoriteDao().observeAll().first().size
        )
    }

    @Test
    fun favoriteSurvivesDatabaseReopening() = runBlocking {
        repository.setFavorite(ArticleType.CHARACTER, "demo-001", true)

        database.close()
        openDatabase()

        assertTrue(
            repository.observeIsFavorite(
                ArticleType.CHARACTER,
                "demo-001"
            ).first()
        )
    }

    @Test
    fun sameIdInDifferentCategoriesRemainsIndependent() = runBlocking {
        val dao = database.favoriteDao()

        repository.setFavorite(ArticleType.CHARACTER, "shared-id", true)
        dao.insert(FavoriteEntity("location", "shared-id"))

        repository.setFavorite(ArticleType.CHARACTER, "shared-id", false)

        assertFalse(
            repository.observeIsFavorite(
                ArticleType.CHARACTER,
                "shared-id"
            ).first()
        )
        assertTrue(
            dao.observeIsFavorite("location", "shared-id").first()
        )
    }

    @Test
    fun exposesKnownReferencesWithoutDeletingUnknownTypes() = runBlocking {
        val dao = database.favoriteDao()

        repository.setFavorite(ArticleType.CHARACTER, "demo-001", true)
        dao.insert(FavoriteEntity("future-category", "other-id"))

        assertEquals(
            listOf(
                FavoriteReference(
                    type = ArticleType.CHARACTER,
                    id = "demo-001"
                )
            ),
            repository.observeFavorites().first()
        )

        assertEquals(2, dao.observeAll().first().size)
    }

    private fun openDatabase() {
        database = Room.databaseBuilder(
            context,
            ArdaDatabase::class.java,
            databaseName
        ).build()
        repository = RoomFavoritesRepository(database.favoriteDao())
    }
}