package sk.mkdigital.androidshowcase.data.network

import sk.mkdigital.androidshowcase.data.dto.auth.AuthResponseDTO
import sk.mkdigital.androidshowcase.data.dto.auth.LoginRequestDTO
import sk.mkdigital.androidshowcase.data.dto.auth.RegisterRequestDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDTO): AuthResponseDTO

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDTO): AuthResponseDTO

    @GET("auth/me")
    suspend fun me(@Header("Authorization") bearer: String): AuthResponseDTO
}
