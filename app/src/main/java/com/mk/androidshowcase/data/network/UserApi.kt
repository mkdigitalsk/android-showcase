package com.mk.androidshowcase.data.network

import com.mk.androidshowcase.data.dto.user.UserResponseDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    suspend fun fetchUser(@Path("id") id: Long): UserResponseDTO

    @GET("users")
    suspend fun fetchUsers(): List<UserResponseDTO>
}
