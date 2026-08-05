package sk.mkdigital.androidshowcase.domain.useCase.location

import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.model.Location
import sk.mkdigital.androidshowcase.domain.repository.LocationRepository
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.base.test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetLastKnownLocationUseCaseTest : BaseTest<GetLastKnownLocationUseCase>() {

    override lateinit var classUnderTest: GetLastKnownLocationUseCase

    @MockK
    private lateinit var locationRepository: LocationRepository

    override fun beforeEach() {
        classUnderTest = GetLastKnownLocationUseCase(locationRepository)
    }

    @Test
    fun `invoke returns last known location from repository`() = runTest {
        val expectedLocation = Location(lat = 48.1486, lon = 17.1077)

        test(
            given = {
                coEvery { locationRepository.lastKnownLocation() } returns expectedLocation
            },
            whenAction = {
                classUnderTest()
            },
            then = {
                assertEquals(expectedLocation, it)
            }
        )
    }
}
