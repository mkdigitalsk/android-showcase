package sk.mkdigital.androidshowcase.domain.useCase.notifications

import sk.mkdigital.androidshowcase.domain.repository.PushNotificationService
import sk.mkdigital.androidshowcase.domain.repository.PushPermissionStatus
import sk.mkdigital.androidshowcase.domain.useCase.base.None
import sk.mkdigital.androidshowcase.domain.useCase.base.UseCase
import javax.inject.Inject

class GetPushPermissionStatusUseCase @Inject constructor(
    private val pushNotificationService: PushNotificationService
) : UseCase<None, PushPermissionStatus>() {
    override suspend fun run(params: None): PushPermissionStatus =
        pushNotificationService.getPermissionStatus()
}
