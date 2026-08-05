package sk.mkdigital.androidshowcase.domain.repository

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.Location

interface LocationRepository {
    suspend fun lastKnownLocation(): Location
    fun locationUpdates(highAccuracy: Boolean = false): Flow<Location>
}
