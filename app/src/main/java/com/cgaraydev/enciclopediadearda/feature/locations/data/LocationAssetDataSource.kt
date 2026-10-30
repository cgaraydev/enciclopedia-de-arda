package com.cgaraydev.enciclopediadearda.feature.locations.data

import android.content.Context
import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocationAssetDataSource @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val decoder: LocationCatalogDecoder
) {
    suspend fun loadLocations(): List<Location> =
        withContext(Dispatchers.IO) {
            val content = context.assets
                .open("demo/locations_list.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            decoder.decode(content)
        }
}