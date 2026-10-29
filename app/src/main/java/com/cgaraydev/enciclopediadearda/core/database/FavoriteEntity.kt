package com.cgaraydev.enciclopediadearda.core.database

import androidx.room.Entity

@Entity(
    tableName = "favorites",
    primaryKeys = ["articleType", "articleId"]
)
data class FavoriteEntity(
    val articleType: String,
    val articleId: String
)