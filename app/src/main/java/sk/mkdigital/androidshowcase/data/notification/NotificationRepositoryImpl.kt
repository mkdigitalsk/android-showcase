package sk.mkdigital.androidshowcase.data.notification

import sk.mkdigital.androidshowcase.data.local.preferences.PersistentPreferences
import sk.mkdigital.androidshowcase.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val persistentPreferences: PersistentPreferences,
) : NotificationRepository {

    override suspend fun getToken(): String? = persistentPreferences.getFcmToken()

    override suspend fun setToken(token: String): Unit = persistentPreferences.setFcmToken(token)
}
