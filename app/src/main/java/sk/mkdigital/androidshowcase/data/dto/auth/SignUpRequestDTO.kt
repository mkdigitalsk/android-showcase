package sk.mkdigital.androidshowcase.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequestDTO(
    val email: String,
    val password: String,
)
