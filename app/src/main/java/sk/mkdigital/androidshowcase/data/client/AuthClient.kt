package sk.mkdigital.androidshowcase.data.client

import sk.mkdigital.androidshowcase.data.dto.auth.AuthResponseDTO
import sk.mkdigital.androidshowcase.data.dto.auth.SignInRequestDTO
import sk.mkdigital.androidshowcase.data.dto.auth.SignUpRequestDTO
import sk.mkdigital.androidshowcase.data.network.AuthApi
import sk.mkdigital.androidshowcase.data.network.handleApiCall
import javax.inject.Inject

interface AuthClient {
    suspend fun signIn(email: String, password: String): AuthResponseDTO
    suspend fun signUp(email: String, password: String): AuthResponseDTO
    suspend fun me(token: String): AuthResponseDTO
}

class AuthClientImpl @Inject constructor(
    private val authApi: AuthApi
) : AuthClient {

    override suspend fun signIn(email: String, password: String): AuthResponseDTO = handleApiCall {
        authApi.signIn(SignInRequestDTO(email, password))
    }

    override suspend fun signUp(email: String, password: String): AuthResponseDTO = handleApiCall {
        authApi.signUp(SignUpRequestDTO(email, password))
    }

    override suspend fun me(token: String): AuthResponseDTO = handleApiCall {
        authApi.me("Bearer $token")
    }
}
