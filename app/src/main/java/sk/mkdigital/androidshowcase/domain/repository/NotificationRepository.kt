package sk.mkdigital.androidshowcase.domain.repository

interface NotificationRepository {
    suspend fun getToken(): String?
    suspend fun setToken(token: String)
}
