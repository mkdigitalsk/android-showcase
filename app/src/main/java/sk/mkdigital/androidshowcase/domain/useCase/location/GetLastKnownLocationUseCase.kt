package sk.mkdigital.androidshowcase.domain.useCase.location

import sk.mkdigital.androidshowcase.domain.model.Location
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetLastKnownLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) : UseCase<None, Location>() {
    override suspend fun run(params: None): Location = locationRepository.lastKnownLocation()
}
