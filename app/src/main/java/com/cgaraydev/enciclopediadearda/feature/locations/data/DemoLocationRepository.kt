package com.cgaraydev.enciclopediadearda.feature.locations.data

import com.cgaraydev.enciclopediadearda.feature.locations.domain.Location
import com.cgaraydev.enciclopediadearda.feature.locations.domain.LocationRepository
import javax.inject.Inject
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DemoLocationRepository @Inject constructor(
    private val dataSource: LocationAssetDataSource
) : LocationRepository {

    private val mutex = Mutex()
    private var cachedLocations: List<Location>? = null

    override suspend fun getLocations(): List<Location> =
        mutex.withLock {
            cachedLocations
                ?: dataSource.loadLocations().also {
                    cachedLocations = it
                }
        }

    override suspend fun getLocationById(id: String): Location? =
        getLocations().firstOrNull { it.id == id }
}