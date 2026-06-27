package com.mk.androidshowcase.data.dto

import com.mk.androidshowcase.data.dto.auth.AuthResponseDTO
import com.mk.androidshowcase.domain.model.AuthSession

fun AuthResponseDTO.toAuthSession() = AuthSession(
    token = token,
    userId = user.id,
    email = user.email,
    name = user.name,
)
