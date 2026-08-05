package sk.mkdigital.androidshowcase.domain.useCase.location

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.Location
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.FlowUseCase
import javax.inject.Inject

class ObserveLocationUpdatesUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) : FlowUseCase<ObserveLocationUpdatesUseCase.Params, Location>() {

    override fun run(params: Params): Flow<Location> =
        locationRepository.locationUpdates(params.highAccuracy)

    data class Params(val highAccuracy: Boolean = false)
}
