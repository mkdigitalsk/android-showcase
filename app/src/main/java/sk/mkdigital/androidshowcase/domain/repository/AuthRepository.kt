package sk.mkdigital.androidshowcase.domain.repository

import sk.mkdigital.androidshowcase.domain.model.AuthSession

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthSession
    suspend fun signUp(email: String, password: String): AuthSession
    suspend fun signInWithToken(): AuthSession?
    suspend fun signOut()
    suspend fun getToken(): String?
}
