package sk.mkdigital.androidshowcase.domain.useCase.notifications

import sk.mkdigital.androidshowcase.domain.model.Notification
import sk.mkdigital.androidshowcase.domain.repository.LocalNotificationService
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class ShowLocalNotificationUseCase @Inject constructor(
    private val localNotificationService: LocalNotificationService
) : UseCase<Notification, Unit>() {
    override suspend fun run(params: Notification) =
        localNotificationService.showNotification(params)
}
