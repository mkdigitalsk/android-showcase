package sk.mkdigital.androidshowcase.data.repository

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.data.location.LocationClient
import sk.mkdigital.androidshowcase.domain.exceptions.base.LocationErrorCode
import sk.mkdigital.androidshowcase.domain.exceptions.base.LocationException
import sk.mkdigital.androidshowcase.domain.model.Location
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationClient: LocationClient
) : LocationRepository {

    override suspend fun lastKnownLocation(): Location {
        return locationClient.lastKnown() ?: throw LocationException(
            message = "Last known location not available",
            userMessage = "Location not available. Please enable location services.",
            errorCode = LocationErrorCode.NOT_AVAILABLE
        )
    }

    override fun locationUpdates(highAccuracy: Boolean): Flow<Location> {
        return locationClient.updates(highAccuracy)
    }
}
