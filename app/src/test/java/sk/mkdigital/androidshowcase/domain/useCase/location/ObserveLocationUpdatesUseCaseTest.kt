package sk.mkdigital.androidshowcase.domain.useCase.location

import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.model.Location
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.base.test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ObserveLocationUpdatesUseCaseTest : BaseTest<ObserveLocationUpdatesUseCase>() {

    override lateinit var classUnderTest: ObserveLocationUpdatesUseCase

    @MockK
    private lateinit var locationRepository: LocationRepository

    override fun beforeEach() {
        classUnderTest = ObserveLocationUpdatesUseCase(locationRepository)
    }

    @Test
    fun `invoke emits location updates from repository`() = runTest {
        val expectedLocation = Location(lat = 48.1486, lon = 17.1077)

        test(
            given = {
                every { locationRepository.locationUpdates(highAccuracy = true) } returns flowOf(expectedLocation)
            },
            whenAction = {
                classUnderTest(ObserveLocationUpdatesUseCase.Params(highAccuracy = true)).first()
            },
            then = {
                assertEquals(expectedLocation, it)
            }
        )
    }
}
