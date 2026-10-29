package com.cgaraydev.enciclopediadearda.core.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query(
        """
        SELECT EXISTS(
            SELECT 1 FROM favorites
            WHERE articleType = :type AND articleId = :id
        )
        """
    )
    fun observeIsFavorite(type: String, id: String): Flow<Boolean>

    @Query("SELECT * FROM favorites ORDER BY articleType, articleId")
    fun observeAll(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorite: FavoriteEntity)

    @Query(
        """
        DELETE FROM favorites
        WHERE articleType = :type AND articleId = :id
        """
    )
    suspend fun delete(type: String, id: String)
}