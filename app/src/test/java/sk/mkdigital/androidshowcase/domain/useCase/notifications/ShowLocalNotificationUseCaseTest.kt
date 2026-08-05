package sk.mkdigital.androidshowcase.domain.useCase.notifications

import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import sk.mkdigital.androidshowcase.domain.model.Notification
import sk.mkdigital.androidshowcase.domain.model.NotificationChannel
import sk.mkdigital.androidshowcase.domain.repository.LocalNotificationService
import sk.mkdigital.androidshowcase.base.BaseTest
import sk.mkdigital.androidshowcase.base.test
import org.junit.jupiter.api.Test

class ShowLocalNotificationUseCaseTest : BaseTest<ShowLocalNotificationUseCase>() {

    override lateinit var classUnderTest: ShowLocalNotificationUseCase

    @MockK
    private lateinit var localNotificationService: LocalNotificationService

    override fun beforeEach() {
        classUnderTest = ShowLocalNotificationUseCase(localNotificationService)
    }

    @Test
    fun `invoke calls showNotification on service with correct notification`() = runTest {
        val notification = Notification(
            id = "test-id",
            title = "Test Title",
            message = "Test Message",
            channel = NotificationChannel.REMINDERS
        )

        test(
            given = {
                coJustRun { localNotificationService.showNotification(notification) }
            },
            whenAction = {
                classUnderTest(notification)
            },
            then = {
                coVerify(exactly = 1) { localNotificationService.showNotification(notification) }
            }
        )
    }
}
