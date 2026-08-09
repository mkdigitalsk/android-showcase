package sk.mkdigital.androidshowcase.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignInRequestDTO(
    val email: String,
    val password: String,
)
