package sk.mkdigital.androidshowcase.data.network

import sk.mkdigital.androidshowcase.data.dto.user.UserResponseDTO
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    suspend fun fetchUser(@Path("id") id: Long): UserResponseDTO

    @GET("users")
    suspend fun fetchUsers(): List<UserResponseDTO>

    @DELETE("users/me")
    suspend fun deleteMe()
}
