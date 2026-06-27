package com.mk.androidshowcase.data.repository

import com.mk.androidshowcase.data.client.AuthClient
import com.mk.androidshowcase.data.dto.toAuthSession
import com.mk.androidshowcase.data.local.preferences.PersistentPreferences
import com.mk.androidshowcase.domain.model.AuthSession
import com.mk.androidshowcase.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val client: AuthClient,
    private val preferences: PersistentPreferences,
) : AuthRepository {

    override suspend fun login(email: String, password: String): AuthSession {
        val response = client.login(email, password)
        val session = response.toAuthSession()
        preferences.setToken(session.token)
        return session
    }

    override suspend fun register(name: String, email: String, password: String): AuthSession {
        val response = client.register(email, password, name)
        val session = response.toAuthSession()
        preferences.setToken(session.token)
        return session
    }

    override suspend fun loginWithToken(): AuthSession? {
        val token = preferences.getToken() ?: return null
        return runCatching { client.me(token).toAuthSession() }
            .onSuccess { preferences.setToken(it.token) }
            .getOrNull()
    }

    override suspend fun logout() {
        preferences.clearToken()
    }

    override suspend fun getToken(): String? = preferences.getToken()
}
