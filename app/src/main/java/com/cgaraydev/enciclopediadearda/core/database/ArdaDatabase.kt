package com.cgaraydev.enciclopediadearda.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [FavoriteEntity::class],
    version = 1,
    exportSchema = true
)
abstract class ArdaDatabase : RoomDatabase() {
    abstract fun favoriteDao(): FavoriteDao
}