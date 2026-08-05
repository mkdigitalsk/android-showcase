package sk.mkdigital.androidshowcase.domain.useCase.notifications

import sk.mkdigital.androidshowcase.domain.repository.PushNotificationService
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class RefreshPushTokenUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : UseCase<None, Unit>() {
    override suspend fun run(params: None) = pushNotificationService.refreshToken()
}
