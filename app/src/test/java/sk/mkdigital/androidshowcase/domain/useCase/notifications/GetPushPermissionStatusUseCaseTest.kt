package sk.mkdigital.androidshowcase.domain.useCase.notifications

import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.repository.PushNotificationService
import sk.mkdigital.androidshowcase.domain.repository.PushPermissionStatus
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.domain.useCase.base.invoke
import sk.mkdigital.androidshowcase.base.test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetPushPermissionStatusUseCaseTest : BaseTest<GetPushPermissionStatusUseCase>() {

    override lateinit var classUnderTest: GetPushPermissionStatusUseCase

    @MockK
    private lateinit var pushNotificationService: PushNotificationService

    override fun beforeEach() {
        classUnderTest = GetPushPermissionStatusUseCase(pushNotificationService)
    }

    @Test
    fun `invoke returns GRANTED when permission is granted`() = runTest {
        test(
            given = {
                every { pushNotificationService.getPermissionStatus() } returns PushPermissionStatus.GRANTED
            },
            whenAction = {
                classUnderTest()
            },
            then = {
                assertEquals(PushPermissionStatus.GRANTED, it)
            }
        )
    }

    @Test
    fun `invoke returns DENIED when permission is denied`() = runTest {
        test(
            given = {
                every { pushNotificationService.getPermissionStatus() } returns PushPermissionStatus.DENIED
            },
            whenAction = {
                classUnderTest()
            },
            then = {
                assertEquals(PushPermissionStatus.DENIED, it)
            }
        )
    }

    @Test
    fun `invoke returns NOT_DETERMINED when permission not determined`() = runTest {
        test(
            given = {
                every { pushNotificationService.getPermissionStatus() } returns PushPermissionStatus.NOT_DETERMINED
            },
            whenAction = {
                classUnderTest()
            },
            then = {
                assertEquals(PushPermissionStatus.NOT_DETERMINED, it)
            }
        )
    }
}
