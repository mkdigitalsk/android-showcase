package sk.mkdigital.androidshowcase.data.network

import sk.mkdigital.androidshowcase.data.dto.auth.AuthResponseDTO
import sk.mkdigital.androidshowcase.data.dto.auth.SignInRequestDTO
import sk.mkdigital.androidshowcase.data.dto.auth.SignUpRequestDTO
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/sign-in")
    suspend fun signIn(@Body request: SignInRequestDTO): AuthResponseDTO

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequestDTO): AuthResponseDTO

    @POST("auth/token")
    suspend fun me(@Header("Authorization") bearer: String): AuthResponseDTO
}
