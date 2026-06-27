package com.mk.androidshowcase.data.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestDTO(
    val email: String,
    val password: String,
)
