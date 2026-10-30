package com.cgaraydev.enciclopediadearda.feature.locations.domain

interface LocationRepository {
    suspend fun getLocations(): List<Location>
    suspend fun getLocationById(id: String): Location?
}