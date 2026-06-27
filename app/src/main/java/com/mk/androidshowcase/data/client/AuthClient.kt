package com.mk.androidshowcase.data.client

import com.mk.androidshowcase.data.dto.auth.AuthResponseDTO
import com.mk.androidshowcase.data.dto.auth.LoginRequestDTO
import com.mk.androidshowcase.data.dto.auth.RegisterRequestDTO
import com.mk.androidshowcase.data.network.AuthApi
import com.mk.androidshowcase.data.network.handleApiCall
import javax.inject.Inject

interface AuthClient {
    suspend fun login(email: String, password: String): AuthResponseDTO
    suspend fun register(email: String, password: String, name: String): AuthResponseDTO
    suspend fun me(token: String): AuthResponseDTO
}

class AuthClientImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthClient {

    override suspend fun login(email: String, password: String): AuthResponseDTO = handleApiCall {
        authApi.login(LoginRequestDTO(email, password))
    }

    override suspend fun register(email: String, password: String, name: String): AuthResponseDTO = handleApiCall {
        authApi.register(RegisterRequestDTO(email, password, name))
    }

    override suspend fun me(token: String): AuthResponseDTO = handleApiCall {
        authApi.me("Bearer $token")
    }
}
