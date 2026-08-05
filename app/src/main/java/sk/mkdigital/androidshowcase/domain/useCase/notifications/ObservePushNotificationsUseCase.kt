package sk.mkdigital.androidshowcase.domain.useCase.notifications

import kotlinx.coroutines.flow.Flow
import sk.mkdigital.androidshowcase.domain.model.Notification
import sk.mkdigital.androidshowcase.domain.repository.PushNotificationService
import sk.mkdigital.androidshowcase.domain.useCase.base.FlowUseCase
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import javax.inject.Inject

class ObservePushNotificationsUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : FlowUseCase<None, Notification>() {
    override fun run(params: None): Flow<Notification> = pushNotificationService.notifications
}
