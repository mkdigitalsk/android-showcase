package sk.mkdigital.androidshowcase.domain.useCase.notifications

import sk.mkdigital.androidshowcase.domain.repository.LocalNotificationService
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class CancelAllNotificationsUseCase @Inject constructor(
    private val localNotificationService: LocalNotificationService
) : UseCase<None, Unit>() {
    override suspend fun run(params: None) = localNotificationService.cancelAllNotifications()
}
